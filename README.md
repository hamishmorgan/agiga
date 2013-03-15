# AGiga

Annotated Gigaword Java API and Command Line Tools

**Note: This project is a fork of an upstream repository at [https://code.google.com/p/agig](https://code.google.com/p/agig)**

---

## Fork Changes:

The project has been forked to implement the following changes:

 * Maven building, integration, and deployment.

 * Integration tests to demonstrate the API
 
 * Add Closeable interface to `*Reader` classes.

In addition the following change was planned, but abandoned:

 * Add the production of Standford Core NLP `Annotation` to the readers. This is required for integration with our other projects. It was assumed that this system would have been used to read the Gigaword format, but it turns out that wasn't the case. Consiquently it would have required either a substantial rewrite, or an entirely indepent conversion system. We've no desire to grow the dependencies for this project, so we decided to implement the convertion within the parent project.
 
 * Decoupling of `*Reader` classes from the data source. Currently you can only specify a file path as a string and IO is hard coded internally, which makes it very hard to extend. A substantial re-write would have been required so for the time being I'm just going to ignore that.

## Tasks

This section describes common tasks that developers may wish to perform, collected here for everyones convenience. 

### Sorting out the dependencies.

The project has the following bundled dependencies in the `lib/`.

 * `guava-14.0-rc1.jar`
 * `log4j-1.2.16.jar`
 * `stanford-parser-2012-07-09.jar`
 * `vtd-xml-2.10.jar`

Of these, only VTD-XML is not deployed on a decent maven repository. For those that are available we shall take the latest minor revision equal to or greater than the ones used here. 

For VTD-XML we deploy the artefact to our Maven repository as follows:

```
mvn deploy:deploy-file \
	-Dfile=vtd-xml-2.10.jar \
	-DgroupId=uk.ac.susx.tag \
	-DartifactId=vtd-xml \
	-Dversion=2.10 \
	-Dpackaging=jar \
	-DgeneratePom=true \
	-DrepositoryId=mlcl-thirdparty \
	-Durl=http://k3d.org/nexus/content/repositories/thirdparty/
```

Obviously you're going to need a account details in your `~//m2/settings.xml` for this to work.


### Pulling upstream changes from the SVN repo

When changes are made to the upstream SVN repo 
(at [https://code.google.com/p/agig](https://code.google.com/p/agig)), 
we can pull and merge them into the local repo. First make sure the remote 
is in your git config file `.git/config`:

```
[svn-remote "svn"]
	url = http://agiga.googlecode.com/svn/trunk
	fetch = :refs/remotes/git-svn
```

The pull the changes using the following command:

```
$ git svn rebase
Current branch master is up to date.
```

Note that this works by first rolling back the repo to state it was in before any local changes where made, then replays all the SVN commits on top, finally it reapplies all the local changes on the HEAD.

---

## Original Documentation

### Annotated Gigaword API and Command Line Tools v1.0 - July 21, 2012

This release includes a Java API and command line tools for reading
the Annotated Gigaword dataset XML files. 

### Project Hosting

For the latest version, go to: http://code.google.com/p/agiga

### Command Line Tools

The command line tools provide a convenient way to print human
readable versions of the XML annotations. The entry point is
edu.jhu.agiga.AgigaPrinter and it has the following usage.

```
usage: java edu.jhu.agiga.AgigaPrinter <type> <gzipped input file>
  where <type> is one of:
    words                     (Words only, one sentence per line)
    lemmas                    (Lemmas only, one sentence per line)
    pos                       (Part-of-speech tags)
    ner                       (Named entity types)
    basic-deps                (Basic dependency parses in CONNL-X format)
    col-deps                  (Collapsed dependency parses in CONNL-X format)
    col-ccproc-deps           (Collapsed and propagated dependency parses in CONNL-X format)
    phrase-structure          (Phrase structure parses)
    coref                     (Coreference resolution as SGML similar to MUC)
    stanford-deps             (toString() methods of Stanford dependency parse annotations)
    stanford-phrase-structure (toString() method of Stanford phrase structure parses)
    for-testing-only          (**For use in testing this API only**)
  and where <gzipped input file> is an .xml.gz file
  from Annotated Gigaword
```

For example, to print part-of-speech tags for the file
nyt_eng_199911.xml.gz, we could run:

```
java -cp build/agiga-1.0.jar:lib/* edu.jhu.agiga.AgigaPrinter pos annotated_gigaword/nyt_eng_199911.xml.gz
```
### Java API       

The Java API provides streaming access to the documents in the .xml.gz
files. Two iterators are provided: StreamingDocumentReader and
StreamingSentenceReader. Both of these take as input the path to an
Annotated Gigaword file and an AgigaPrefs object. 

By default, the AgigaPrefs constructor will ensure that every
annotation in the XML is read in and that the resulting objects are
fully populated. However, by turning off certain options, it's
possible to skip the reading and creation of objects corresponding to
unused annotations.

StreamingDocumentReader is an iterator over AgigaDocument objects. The
AgigaDocument class gives access to the coreference resolution (via
AgigaCoref objects) annotations and the sentences (via AgigaSentence
objects).

StreamingSentenceReader is an iterator over AgigaSentence
objects. This bypasses the document level annotations such as coref
and the document ids and provides direct access to the sentence
annotations only.

AgigaPrinter provides examples of how to use these iterators and set
the AgigaPrefs object so that only the necessary annotations are read.
Examples of how to use the Agiga objects can also be found in the
AgigaDocument.write* and AgigaSentence.write* methods.

### One- vs. Zero-Indexing:

In the XML, the sentences and tokens are given Ids that are
one-indexed. However, we find it to be more convenient to work with
zero-indexed **indices** in the Java API. Accordingly, the Java API
does not provide access to these original Ids but instead provides
access to indices. These indices are accessed via methods named
get*Idx(), such as AgigaSentence.getIdx() and
AgigaMention.getSentenceIdx() -- or AgigaToken.getIdx() and
AgigaDependency.getGovIdx(). These indices also correspond to the
ordered elements in the Lists used throughout the API.

Of course, the original Ids from the XML can be recovered by adding
one to the indices in the API. However, we didn't want to confuse the
issue by providing API calls for both.

### Building

A build.xml is provided for building with Apache Ant.  Example
commands are below and should be run from the top level directory that
contains the build.xml.

#### To compile: 
ant

#### To clean and compile
ant clean compile

#### To build jars of classes and sources:
ant jar
