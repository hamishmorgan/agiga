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

import com.google.common.base.Throwables;
import com.google.common.io.Closer;
import org.apache.log4j.*;
import org.junit.*;
import org.junit.rules.TestName;

import java.io.IOException;
import java.util.List;

import static java.text.MessageFormat.format;

/**
 * A collection of integration tests that double as API usage demonstrations.
 *
 * @author Hamish Morgan &lt;hamish.morgan@sussex.ac.uk&gt;
 */
public class StreamingDocumentReaderIntegrationTests {

    private static final Logger LOG = Logger.getLogger(StreamingDocumentReaderIntegrationTests.class);
    private static final String TEST_FILE_PATH = "src/test/resources/edu/jhu/agiga/nyt_eng_201012_sample.xml.gz";

    @Rule()
    public final TestName testName = new TestName();

    @BeforeClass
    public static void configureLogger() {
        final ConsoleAppender cAppender = new ConsoleAppender(new PatternLayout("%d{HH:mm:ss,SSS} [%t] %p %c %x - %m%n"));
        BasicConfigurator.configure(cAppender);
        Logger.getRootLogger().setLevel(Level.DEBUG);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before()
    public final void printTestMethod() throws SecurityException, NoSuchMethodException {
        LOG.info(format(
                "Running test: {0}#{1}",
                this.getClass().getName(), testName.getMethodName()));
    }


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Read the surface text from the documents.
     * <p/>
     * Note that it's not possible to perfectly reconstruct the plain text documents, in particular the white-space
     * is entirely lost. Consequently, we end up will rather silly spacing such as a space on both sides of a comma.
     */
    @Test
    public void testGetText() throws IOException {

        final Closer closer = Closer.create();

        try {
            final AgigaPrefs prefs = new AgigaPrefs();
            final StreamingDocumentReader instance =
                    closer.register(new StreamingDocumentReader(TEST_FILE_PATH, prefs));

            int documentCount = 0;
            for (final AgigaDocument doc : instance) {
                ++documentCount;

                LOG.debug(format("Reading document {2}; id={0}, type={1}",
                        doc.getDocId(), doc.getType(), documentCount));

                final StringBuilder sentBuilder = new StringBuilder();
                for (AgigaSentence sent : doc.getSents()) {

                    for (AgigaToken tok : sent.getTokens()) {
                        sentBuilder.append(tok.getWord());
                        sentBuilder.append(' ');
                    }
                    sentBuilder.append('\n');
                }
                System.out.println(sentBuilder.toString());

            }
            LOG.info("Number of docs: " + instance.getNumDocs());
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    @Test
    public void testGetGramRels() throws IOException {
        final Closer closer = Closer.create();

        try {
            final AgigaPrefs prefs = new AgigaPrefs();
            final StreamingDocumentReader instance =
                    closer.register(new StreamingDocumentReader(TEST_FILE_PATH, prefs));

            int documentCount = 0;
            for (final AgigaDocument doc : instance) {
                ++documentCount;

                LOG.debug(format("Reading document {2}; id={0}, type={1}",
                        doc.getDocId(), doc.getType(), documentCount));

                final StringBuilder sentBuilder = new StringBuilder();
                for (AgigaSentence sent : doc.getSents()) {
                    final List<AgigaToken> tokens = sent.getTokens();

                    for(AgigaTypedDependency rel : sent.getColCcprocDeps()) {
                        final int depIdx = rel.getDepIdx();
                        final int govIdx = rel.getGovIdx();
                        final String type = rel.getType();
                        final String dep = tokens.get(depIdx).getWord();
                        final String gov = govIdx == -1 ? "<NIL>" : tokens.get(govIdx).getWord();

                        System.out.printf("%s %s:%s%n", gov,type, dep);
                    }
                }
            }
            LOG.info("Number of docs: " + instance.getNumDocs());
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    /**
     * Test the new Closeable interface on the *Reader classes.
     */
    @Test
    public void testCloseable() throws IOException {

        final AgigaPrefs prefs = new AgigaPrefs();
        final StreamingDocumentReader instance = new StreamingDocumentReader(TEST_FILE_PATH, prefs);

        Assert.assertTrue("Instance does not have a first item.", instance.hasNext());

        instance.close();

        try {
            instance.hasNext();
            Assert.fail("hasNext should have thrown an exception.");
        } catch (RuntimeException ex) {
            // We are expecting an IOException (stream closed) wrapped in a RuntimeException so it matches the
            // iterator signature. It would be nicer if hasNext() just returned false, but that would require
            // a more substantial rewrite.
            if (!Throwables.getRootCause(ex).getClass().equals(IOException.class))
                Assert.fail("Expecting IOException as root cause, but found " + Throwables.getRootCause(ex).getClass());
        }
    }


}
