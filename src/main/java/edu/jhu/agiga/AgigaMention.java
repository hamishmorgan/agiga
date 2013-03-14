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

import java.io.Serializable;

/**
 * Each AgigaMention object represents a single mention of an entity for
 * coreference resolution within a document. Annotated Gigaword does not
 * directly provide a unique identifier for each entity in a document. However,
 * AgigaDocument.assignMucStyleIdsAndRefsToMentions() can be used if such ids
 * are needed -- this method will set the mucId and mucRef fields as if they
 * were the ID and REF attributes of a MUC-7 mention.
 * 
 * @author mgormley
 * 
 */
public class AgigaMention implements Serializable {

	public static final long serialVersionUID = 1;

    public static final int UNASSIGNED = -1;
    // Agiga annotations
    private boolean isRepresentative;
    private int sentenceIdx;
    private int startTokenIdx;
    private int endTokenIdx;
    private int headTokenIdx;

    // Additional annotations for MUC style printout
    private int mucId;
    private int mucRef;

    public AgigaMention(boolean isRepresentative, int sentenceIdx, int startTokenIdx, int endTokenIdx, int headTokenIdx) {
        this.isRepresentative = isRepresentative;
        this.sentenceIdx = sentenceIdx;
        this.startTokenIdx = startTokenIdx;
        this.endTokenIdx = endTokenIdx;
        this.headTokenIdx = headTokenIdx;
        this.mucId = UNASSIGNED;
        this.mucRef = UNASSIGNED;
    }

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(isRepresentative,
			sentenceIdx, startTokenIdx, endTokenIdx, headTokenIdx, mucId, mucRef);
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof AgigaMention) {
			AgigaMention o = (AgigaMention) other;
			return isRepresentative == o.isRepresentative
				&& sentenceIdx == o.sentenceIdx
				&& startTokenIdx == o.startTokenIdx
				&& endTokenIdx == o.endTokenIdx
				&& headTokenIdx == o.headTokenIdx
				&& mucId == o.mucId
				&& mucRef == o.mucRef;
		}
		return false;
	}

    public boolean isRepresentative() {
        return isRepresentative;
    }

    public int getSentenceIdx() {
        return sentenceIdx;
    }

    public int getStartTokenIdx() {
        return startTokenIdx;
    }

    public int getEndTokenIdx() {
        return endTokenIdx;
    }

    public int getHeadTokenIdx() {
        return headTokenIdx;
    }

    public int getMucId() {
        return mucId;
    }

    public int getMucRef() {
        return mucRef;
    }

    public void setMucId(int id) {
        this.mucId = id;
    }

    public void setMucRef(int ref) {
        this.mucRef = ref;
    }

    @Override
    public String toString() {
        return "AgigaMention [endTokenId=" + endTokenIdx + ", headTokenId=" + headTokenIdx + ", id=" + mucId
                + ", isRepresentative=" + isRepresentative + ", ref=" + mucRef + ", sentenceId=" + sentenceIdx
                + ", startTokenId=" + startTokenIdx + "]";
    }

}
