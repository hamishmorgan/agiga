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

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Each AgigaCoref object provides access to all the mentions of a single entity
 * in a document. These coreference resolution annotations are represented as a
 * list of coref mentions, or AgigaMention objects.
 * 
 * @author mgormley
 * 
 */
public class AgigaCoref implements Serializable {

	public static final long serialVersionUID = 1;

    private List<AgigaMention> mentions;

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof AgigaCoref) {
			AgigaCoref o = (AgigaCoref) other;
			return com.google.common.base.Objects.equal(mentions, o.mentions);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(mentions);
	}
    
    public AgigaCoref() {
        this.mentions = new ArrayList<AgigaMention>();
    }

    public List<AgigaMention> getMentions() {
        return mentions;
    }

    public void add(AgigaMention mention) {
        mentions.add(mention);
    }
        
}
