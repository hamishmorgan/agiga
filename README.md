# agiga

Annotated Gigaword Java API and Command Line Tools

**Note: This project is a fork of an upstream repository at [https://code.google.com/p/agig](https://code.google.com/p/agig)**


## Fork Changes:

The project has been forked to implement the following changes:

 * Maven building, integration, and deployment.

 * Integration tests to demonstrate the API

## Tasks

This section describes common tasks that developers may wish to perform, collected here for everyones convenience. 

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



