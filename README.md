# agiga

Annotated Gigaword Java API and Command Line Tools

**Note: This project is a fork of an upstream repository at [https://code.google.com/p/agig](https://code.google.com/p/agig)**


## Fork Changes:

The project has been forked to implement the following changes:

 * Maven building, integration, and deployment.

 * Integration tests to demonstrate the API

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

When changes are made to the upstream SVN repo (at [https://code.google.com/p/agig](https://code.google.com/p/agig)), we can pull and merge them into the local repo. First make sure the remote is in your git config file `.git/config`:

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



