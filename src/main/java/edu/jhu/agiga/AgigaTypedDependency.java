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
 * Each AgigaTypedDependency represents an arc in a dependency tree. It provides
 * access to the type of dependency arc. It also provides the indices of the
 * governor and dependent. These are zero-indexed and correspond to the indices
 * of the AgigaToken objects in the AgigaSentence object.
 * 
 * @author mgormley
 * 
 */
public class AgigaTypedDependency implements Serializable {
	
	public static final long serialVersionUID = 1;

    private String type;
    private int gov;
    private int dep;

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof AgigaTypedDependency) {
			AgigaTypedDependency o = (AgigaTypedDependency) other;
			return com.google.common.base.Objects.equal(type, o.type)
				&& gov == o.gov
				&& dep == o.dep;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(type, gov, dep);
	}
	

    public AgigaTypedDependency(String type, int gov, int dep) {
        this.type = type;
        this.gov = gov;
        this.dep = dep;
    }

    public String getType() {
        return type;
    }

    public int getGovIdx() {
        return gov;
    }

    public int getDepIdx() {
        return dep;
    }

}
