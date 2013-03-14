package edu.jhu.agiga;

import java.io.Serializable;

/**
 * An AgigaToken object represents a single token and its annotations in the
 * AnnotatedGigaword corpus. This provides access to the word, the lemma, the
 * optional normalized named entity, the part-of-speech tag, the named entity
 * type, the character offset indices of the beginning and the end of the token,
 * and the token id.
 * 
 * @author mgormley
 * 
 */
public class AgigaToken implements Serializable {
	
	public static final long serialVersionUID = 1;

    private int tokIdx;
    private String word;
    private String lemma;
    private int charOffBegin;
    private int charOffEnd;
    private String posTag;
    private String nerTag;
    private String normNer;

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof AgigaToken) {
			AgigaToken o = (AgigaToken) other;
			return tokIdx == o.tokIdx
				&& com.google.common.base.Objects.equal(word, o.word)
				&& com.google.common.base.Objects.equal(lemma, o.lemma)
				&& charOffBegin == o.charOffBegin
				&& charOffEnd == o.charOffEnd
				&& com.google.common.base.Objects.equal(posTag, o.posTag)
				&& com.google.common.base.Objects.equal(nerTag, o.nerTag)
				&& com.google.common.base.Objects.equal(normNer, o.normNer);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(
			tokIdx, word, lemma, charOffBegin, charOffEnd, posTag, nerTag, normNer);
	}

    public AgigaToken() {
        // No argument constructor
    }
    
    public AgigaToken(String word, String lemma, int charOffBegin, int charOffEnd, String posTag, String nerTag,
            String normNer) {
        this.word = word;
        this.lemma = lemma;
        this.charOffBegin = charOffBegin;
        this.charOffEnd = charOffEnd;
        this.posTag = posTag;
        this.nerTag = nerTag;
        this.normNer = normNer;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public int getCharOffBegin() {
        return charOffBegin;
    }

    public void setCharOffBegin(int charOffBegin) {
        this.charOffBegin = charOffBegin;
    }

    public int getCharOffEnd() {
        return charOffEnd;
    }

    public void setCharOffEnd(int charOffEnd) {
        this.charOffEnd = charOffEnd;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public String getNerTag() {
        return nerTag;
    }

    public void setNerTag(String nerTag) {
        this.nerTag = nerTag;
    }

    public String getNormNer() {
        return normNer;
    }

    public void setNormNer(String normNer) {
        this.normNer = normNer;
    }

    public void setTokIdx(int tokIdx) {
        this.tokIdx = tokIdx;
    }
    
    public int getTokIdx() {
        return tokIdx;
    }

}
