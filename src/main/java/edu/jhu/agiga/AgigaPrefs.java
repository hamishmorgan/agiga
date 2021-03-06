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

import edu.jhu.agiga.AgigaConstants.DependencyForm;

/**
 * AgigaPrefs determines which of the annotations are read from XML.
 * 
 * By default, the AgigaPrefs constructor will ensure that every annotation in
 * the XML is read in and that the resulting objects are fully populated.
 * However, by turning off certain options, it's possible to skip the reading
 * and creation of objects corresponding to unused annotations. Doing so can
 * provide significant memory savings.
 * 
 * @author mgormley
 * 
 */
public class AgigaPrefs implements Serializable {

	public static final long serialVersionUID = 1;

    boolean readWord;
    boolean readLemma;
    boolean readOffsets;
    boolean readPos;
    boolean readNer;
    boolean readNormNer;
    boolean readParse;
    boolean readBasicDeps;
    boolean readColDeps;
    boolean readColCcprocDeps;
	boolean readCoref;

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof AgigaPrefs) {
			AgigaPrefs o = (AgigaPrefs) other;
			return readWord == o.readWord
				&& readLemma == o.readLemma
				&& readOffsets == o.readOffsets
				&& readPos == o.readPos
				&& readNer == o.readNer
				&& readNormNer == o.readNormNer
				&& readParse == o.readParse
				&& readBasicDeps == o.readBasicDeps
				&& readColDeps == o.readColDeps
				&& readColCcprocDeps == o.readColCcprocDeps
				&& readCoref == o.readCoref;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(readWord,
			readLemma, readOffsets, readPos, readNer, readNormNer,
			readParse, readBasicDeps, readColDeps, readColCcprocDeps, readCoref);
	}

    public AgigaPrefs() {
        setAll(true);
    }

    public void setAll(boolean value) {
        readWord = value;
        readLemma = value;
        readOffsets = value;
        readPos = value;
        readNer = value;
        readNormNer = value;
        readParse = value;
        readBasicDeps = value;
        readColDeps = value;
        readColCcprocDeps = value;
        readCoref = value;
    }

    public void setForConnlStyleDeps(DependencyForm form) {
        setAll(false);
        setWord(true);
        setLemma(true);
        setPos(true);
        setDeps(form);
    }

    public void setDeps(DependencyForm form) {
        if (form == DependencyForm.BASIC_DEPS) {
            setBasicDeps(true);
        } else if (form == DependencyForm.COL_DEPS) {
            setColDeps(true);
        } else if (form == DependencyForm.COL_CCPROC_DEPS) {
            setColCcprocDeps(true);
        } else {
            throw new IllegalStateException("Unsupported DependencyForm: " + form);
        }
    }

    public void setWord(boolean readWord) {
        this.readWord = readWord;
    }

    public void setLemma(boolean readLemma) {
        this.readLemma = readLemma;
    }

    public void setOffsets(boolean readOffsets) {
        this.readOffsets = readOffsets;
    }

    public void setPos(boolean readPos) {
        this.readPos = readPos;
    }

    public void setNer(boolean readNer) {
        this.readNer = readNer;
    }

    public void setNormNer(boolean readNormNer) {
        this.readNormNer = readNormNer;
    }

    public void setParse(boolean readParse) {
        this.readParse = readParse;
    }

    public void setBasicDeps(boolean readBasicDeps) {
        this.readBasicDeps = readBasicDeps;
    }

    public void setColDeps(boolean readColDeps) {
        this.readColDeps = readColDeps;
    }

    public void setColCcprocDeps(boolean readColCcprocDeps) {
        this.readColCcprocDeps = readColCcprocDeps;
    }

    public void setCoref(boolean readCoref) {
        this.readCoref = readCoref;
    }

}
