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

package org.amanzi.neo.loader.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.amanzi.neo.loader.core.internal.IConfiguration;
import org.amanzi.neo.loader.core.internal.Loader;
import org.amanzi.neo.loader.core.parser.IParser;
import org.amanzi.neo.loader.core.saver.ISaver;
import org.amanzi.neo.loader.core.validator.IValidationResult;
import org.amanzi.neo.loader.core.validator.IValidationResult.Result;
import org.amanzi.neo.loader.core.validator.IValidator;
import org.amanzi.testing.AbstractMockitoTest;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class LoaderTest extends AbstractMockitoTest {

    private static final String LOADER_NAME = "Loader Name";

    /**
     * TODO Purpose of
     * <p>
     * </p>
     * 
     * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
     * @since 1.0.0
     */
    private final class ParserAnswer implements Answer<Boolean> {
        @Override
        public Boolean answer(final InvocationOnMock invocation) {
            if (++hasNextCall > ELEMENT_NUMBER) {
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        }
    }

    private static final int SAVER_NUMBER = 3;

    private static final int ELEMENT_NUMBER = 3;

    private Loader<IConfiguration, IData> loader;

    private IParser<IConfiguration, IData> parser;

    private List<ISaver<IConfiguration, IData>> savers;

    private IConfiguration configuration;

    private IData data;

    private IValidator<IConfiguration> validator;

    private int hasNextCall;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        loader = new Loader<IConfiguration, IData>();
        loader.setName(LOADER_NAME);

        parser = mock(IParser.class);
        loader.setParser(parser);

        savers = new ArrayList<ISaver<IConfiguration, IData>>();
        for (int i = 0; i < SAVER_NUMBER; i++) {
            ISaver<IConfiguration, IData> saver = mock(ISaver.class);
            savers.add(saver);

            loader.addSaver(saver);
        }
        validator = mock(IValidator.class);
        loader.setValidator(validator);

        configuration = mock(IConfiguration.class);
        data = mock(IData.class);

        hasNextCall = 0;
        when(parser.next()).thenReturn(data);
        when(parser.hasNext()).thenAnswer(new ParserAnswer());
    }

    @Test
    public void testCheckLoaderActivityOnInitialization() throws Exception {
        loader.init(configuration);

        verify(parser).init(configuration);
        for (ISaver<IConfiguration, IData> saver : savers) {
            verify(saver).init(configuration);
        }
    }

    @Test
    public void testCheckActivityOnRun() throws Exception {
        IProgressMonitor monitor = mock(IProgressMonitor.class);

        loader.run(monitor);

        verify(parser).setProgressMonitor(LOADER_NAME, monitor);

        verify(parser, times(ELEMENT_NUMBER + 1)).hasNext();
        verify(parser, times(ELEMENT_NUMBER)).next();

        for (ISaver<IConfiguration, IData> saver : savers) {
            verify(saver, times(ELEMENT_NUMBER)).save(data);
        }
    }

    @Test
    public void testCheckActivityOnValidationWithSuccessConfiguration() throws Exception {
        when(configuration.isValid()).thenReturn(IValidationResult.SUCCESS);

        loader.validate(configuration);

        verify(configuration).isValid();
        verify(validator).validate(configuration);
    }

    @Test
    public void testCheckActivityOnValidationWithFailedConfiguration() throws Exception {
        when(configuration.isValid()).thenReturn(IValidationResult.FAIL);

        loader.validate(configuration);

        verify(configuration).isValid();
        verify(validator, never()).validate(configuration);
    }

    @Test
    public void testCheckResultOnValidation() throws Exception {
        IValidationResult validationResult = IValidationResult.SUCCESS;

        when(configuration.isValid()).thenReturn(validationResult);
        when(validator.validate(configuration)).thenReturn(validationResult);

        IValidationResult result = loader.validate(configuration);

        assertEquals("Unexpected validation result", validationResult, result);
    }

    @Test
    public void testCheckActivityOnIsAppropriate() throws Exception {
        when(validator.appropriate(configuration)).thenReturn(IValidationResult.SUCCESS);

        loader.isAppropriate(configuration);

        verify(validator).appropriate(configuration);
    }

    @Test
    public void testCheckSuccessResultOnIsAppropriate() throws Exception {
        when(validator.appropriate(configuration)).thenReturn(IValidationResult.SUCCESS);

        IValidationResult result = loader.isAppropriate(configuration);

        assertEquals("validation result should be success", Result.SUCCESS, result.getResult());
    }

    @Test
    public void testCheckFailResultOnIsAppropriate() throws Exception {
        when(validator.appropriate(configuration)).thenReturn(IValidationResult.FAIL);

        IValidationResult result = loader.isAppropriate(configuration);

        assertEquals("validation result should be fail", Result.FAIL, result.getResult());
    }

    @Test
    public void testCheckUnkonwnResultOnIsAppropriate() throws Exception {
        when(validator.appropriate(configuration)).thenReturn(IValidationResult.UNKNOWN);

        IValidationResult result = loader.isAppropriate(configuration);

        assertEquals("validation result should be unknown", Result.UNKNOWN, result.getResult());
    }

    @Test
    public void testCheckFinishUpAction() throws Exception {
        IProgressMonitor monitor = mock(IProgressMonitor.class);

        loader.run(monitor);

        verify(parser).finishUp();
        for (ISaver<IConfiguration, IData> saver : savers) {
            verify(saver).finishUp();
        }
    }

    @Test
    public void testCheckFinishUpWhenException() throws Exception {
        File mockedFile = mock(File.class);
        when(mockedFile.getName()).thenReturn(StringUtils.EMPTY);
        when(parser.next()).thenThrow(new IllegalArgumentException());
        when(parser.getLastParsedFile()).thenReturn(mockedFile);
        when(parser.getLastParsedLineNumber()).thenReturn(0);
        doNothing().when(parser).finishUp();

        for (ISaver<IConfiguration, IData> saver : savers) {
            doNothing().when(saver).finishUp();
        }

        loader.run(null);

        verify(parser).finishUp();
        for (ISaver<IConfiguration, IData> saver : savers) {
            verify(saver).finishUp();
        }
    }
}
