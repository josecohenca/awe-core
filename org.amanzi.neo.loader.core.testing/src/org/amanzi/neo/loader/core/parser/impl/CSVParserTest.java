/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.neo.loader.core.parser.impl;

import java.io.EOFException;
import java.io.File;
import java.io.InputStreamReader;

import org.amanzi.neo.loader.core.ISingleFileConfiguration;
import org.amanzi.testing.AbstractMockitoTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import au.com.bytecode.opencsv.CSVReader;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class CSVParserTest extends AbstractMockitoTest {

    private static final String[] HEADERS = {"header1", "header2", "header3"};

    private static final String[] VALUES = {"value1", "value2", "value3"};

    private static final String[] VALUES_2 = {"value4", "value5", "value6"};

    private static final String[][] CSV_DATA = {HEADERS, VALUES, VALUES_2};

    /**
     * TODO Purpose of
     * <p>
     * </p>
     * 
     * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
     * @since 1.0.0
     */
    private final class CSV_READER_ANSWER implements Answer<String[]> {
        @Override
        public String[] answer(InvocationOnMock invocation) throws Throwable {
            if (dataIndex < CSV_DATA.length) {
                return CSV_DATA[dataIndex++];
            } else {
                throw new EOFException();
            }
        }
    }

    private CSVParser parser;

    private ISingleFileConfiguration configuration;

    private File file;

    private int dataIndex;

    private CSVReader reader;

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile("cp1", "file");

        configuration = mock(ISingleFileConfiguration.class);
        when(configuration.getFile()).thenReturn(file);

        parser = spy(new CSVParser());

        dataIndex = 0;
        reader = mock(CSVReader.class);
        when(reader.readNext()).thenAnswer(new CSV_READER_ANSWER());

    }

    @Test
    public void testCheckActionsOnInitialization() throws Exception {
        parser.init(configuration);

        verify(parser).initializeCSVReader(any(InputStreamReader.class));
    }

    @Test
    public void testCheckCSVReaderActionsOnParsing() throws Exception {
        doReturn(reader).when(parser).initializeCSVReader(any(InputStreamReader.class));
        parser.init(configuration);

        while (parser.hasNext()) {
            parser.next();
        }

        verify(parser, times(CSV_DATA.length)).parseNextElement();
    }

    @Test
    public void testCheckCountOfParsing() throws Exception {
        doReturn(reader).when(parser).initializeCSVReader(any(InputStreamReader.class));
        parser.init(configuration);

        int count = 0;
        while (parser.hasNext()) {
            count++;
            parser.next();
        }

        assertEquals("Unexpected size of parsed elements", CSV_DATA.length - 1, count);
    }
}
