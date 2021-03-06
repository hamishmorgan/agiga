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

import java.io.IOException;
import java.io.Writer;
import java.io.Serializable;
import java.util.List;

import edu.jhu.agiga.AgigaConstants.DependencyForm;
import edu.stanford.nlp.ling.WordLemmaTag;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * AgigaSentence provides access to the sentence-level annotations: AgigaToken
 * objects, AgigaTypedDependency objects for the three dependency forms, the
 * contituency parse as text, and the Stanford parser API objects for
 * dependencies and constituency parses.
 * 
 * This class also provides methods for writing out these various annotations in
 * human-readable formats and some common machine-readable formats.
 * 
 * @author mgormley
 * 
 */
public interface AgigaSentence extends Serializable {

	@Override public boolean equals(Object other);
	@Override public int hashCode();

    public int getSentIdx();

    public List<AgigaToken> getTokens();

    public String getParseText();

    public List<AgigaTypedDependency> getAgigaDeps(DependencyForm form);

    public List<AgigaTypedDependency> getBasicDeps();

    public List<AgigaTypedDependency> getColDeps();

    public List<AgigaTypedDependency> getColCcprocDeps();
    
    // -------- Stanford API methods --------
    public List<WordLemmaTag> getStanfordWordLemmaTags();

    public List<TypedDependency> getStanfordTypedDependencies(DependencyForm form);

    public List<TreeGraphNode> getStanfordTreeGraphNodes(DependencyForm form);

    public Tree getStanfordContituencyTree();

    // -------- Printing methods --------    
    public void writeWords(Writer writer) throws IOException;
    
    public void writeLemmas(Writer writer) throws IOException;

    public void writeTokens(Writer writer, boolean useLemmas, boolean useNormNer) throws IOException;
    
    public void writePosTags(Writer writer) throws IOException;

    public void writeNerTags(Writer writer) throws IOException;

    public void writeTags(Writer writer, boolean useLemmas, boolean useNormNer, boolean useNerTags) throws IOException;

    public void writeConnlStyleDeps(Writer writer, DependencyForm form) throws IOException;

    public void writeParseText(Writer writer) throws IOException;

}
