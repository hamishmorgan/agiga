/*
 * Copyright (c) 2012-2013, Johns Hopkins University
 * Copyright (c) 2012-2013, University of Sussex
 * All rights reserved.
 *
 * This file is part of Agiga.
 *
 * Agiga is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Agiga is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Agiga.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.jhu.agiga;

import static edu.jhu.agiga.AgigaSentenceReader.require;

import java.io.IOException;
import java.io.StringReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.jhu.agiga.AgigaConstants.DependencyForm;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.WordLemmaTag;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFactory;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * Extends BasicAgigaSentence to implement methods for constructing the Stanford
 * parser API objects.
 * 
 * @author mgormley
 * 
 */
public class StanfordAgigaSentence extends BasicAgigaSentence implements AgigaSentence, Serializable {
	
	public static final long serialVersionUID = 1;

    private static final Label ROOT_LABEL = new WordLemmaTag("ROOT");
    
    List<TreeGraphNode> nodes = null;

    public StanfordAgigaSentence(AgigaPrefs prefs) {
        super(prefs);
    }

    // The Stanford TreeGraphNode throws away all but the word from
    // the WordLemmaTag label in converting it to a CoreLabel. Accordingly
    // we allow access to the labels here as well.
    /* (non-Javadoc)
     * @see edu.jhu.hltcoe.sp.data.depparse.AgigaSentence#getStanfordWordLemmaTags()
     */
    public List<WordLemmaTag> getStanfordWordLemmaTags() {
        List<AgigaToken> tokens = getTokens();
        List<WordLemmaTag> labels = new ArrayList<WordLemmaTag>();
        for (int i = 0; i < tokens.size(); i++) {
            AgigaToken at = tokens.get(i);
            WordLemmaTag curToken;
            require(prefs.readWord, "AgigaPrefs.readWord must be true for getStanfordWordLemmaTags()");
            if (prefs.readWord && prefs.readLemma && prefs.readPos) {
                curToken = new WordLemmaTag(at.getWord(), at.getLemma(), at.getPosTag());
            } else if (prefs.readWord && prefs.readPos) {
                curToken = new WordLemmaTag(at.getWord(), at.getPosTag());
            } else { // if (prefs.readWord) {
                curToken = new WordLemmaTag(at.getWord());
            }
            labels.add(curToken);
        }
        return labels;
    }

    /* (non-Javadoc)
     * @see edu.jhu.hltcoe.sp.data.depparse.AgigaSentence#getStanfordTypedDependencies(edu.jhu.hltcoe.sp.data.depparse.AgigaConstants.DependencyForm)
     */
    public List<TypedDependency> getStanfordTypedDependencies(DependencyForm form) {
        List<TypedDependency> dependencies = new ArrayList<TypedDependency>();
        if (this.nodes == null)
        	nodes = getStanfordTreeGraphNodes(form);

        List<AgigaTypedDependency> agigaDeps = getAgigaDeps(form);
        for (AgigaTypedDependency agigaDep : agigaDeps) {
            // Add one, since the tokens are zero-indexed but the TreeGraphNodes are one-indexed
            TreeGraphNode gov = nodes.get(agigaDep.getGovIdx() + 1);
            TreeGraphNode dep = nodes.get(agigaDep.getDepIdx() + 1);
            // Create the typed dependency
            TypedDependency typedDep = new TypedDependency(GrammaticalRelation.valueOf(agigaDep.getType()), gov, dep);
            dependencies.add(typedDep);
        }
        return dependencies;
    }

    /* (non-Javadoc)
     * @see edu.jhu.hltcoe.sp.data.depparse.AgigaSentence#getStanfordTreeGraphNodes(edu.jhu.hltcoe.sp.data.depparse.AgigaConstants.DependencyForm)
     */
    public List<TreeGraphNode> getStanfordTreeGraphNodes(DependencyForm form) {
    	if (this.nodes != null) return this.nodes;
    	
        this.nodes = new ArrayList<TreeGraphNode>();
        // Add an explicit root node
        nodes.add(new TreeGraphNode(ROOT_LABEL));
        
        List<WordLemmaTag> labels = getStanfordWordLemmaTags();
        for (WordLemmaTag curToken : labels) {
            // Create the tree node
            TreeGraphNode treeNode = new TreeGraphNode(curToken);
            treeNode.label().setTag(curToken.tag());
            /**
             * Caution, the order to call is to first setWord(), then setlemma()
             * From the Stanford source code:
             *   
  public void setWord(String word) {
    set(WordAnnotation.class, word);
    // pado feb 09: if you change the word, delete the lemma.
    remove(LemmaAnnotation.class);
  }
  
    public void setLemma(String lemma) {
    set(LemmaAnnotation.class, lemma);
  }
              */
            treeNode.label().setWord(curToken.word());
            treeNode.label().setLemma(curToken.lemma());
            nodes.add(treeNode);
        }

        List<AgigaTypedDependency> agigaDeps = getAgigaDeps(form);
        for (AgigaTypedDependency agigaDep : agigaDeps) {
            // Add one, since the tokens are zero-indexed but the TreeGraphNodes are one-indexed
            TreeGraphNode gov = nodes.get(agigaDep.getGovIdx() + 1);
            TreeGraphNode dep = nodes.get(agigaDep.getDepIdx() + 1);

            // Add gov/dep to TreeGraph
            gov.addChild(dep);
            dep.setParent(gov);
            require(dep.parent() == gov);
        }

        return nodes;
    }
    
    /* (non-Javadoc)
     * @see edu.jhu.hltcoe.sp.data.depparse.AgigaSentence#getStanfordContituencyTree()
     */
    public Tree getStanfordContituencyTree() {
        TreeFactory tf = new LabeledScoredTreeFactory();
        StringReader r = new StringReader(getParseText());
        TreeReader tr = new PennTreeReader(r, tf);
        try {
            return tr.readTree();
        } catch (IOException e) {
            throw new RuntimeException("Error: IOException should not be thrown by StringReader");
        }
    }
    
}
