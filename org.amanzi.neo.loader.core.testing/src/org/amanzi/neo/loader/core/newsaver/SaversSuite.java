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

package org.amanzi.neo.loader.core.newsaver;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <p>
 * Suite for unit tests
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
@RunWith(Suite.class)
@SuiteClasses({org.amanzi.neo.loader.core.newsaver.NewNetworkSaverTesting.class,
        org.amanzi.neo.loader.core.newsaver.NewNeighbourSaverTesting.class,
        org.amanzi.neo.loader.core.newsaver.NewTemsSaverTesting.class,
        org.amanzi.neo.loader.core.newsaver.NewRomesSaverTesting.class,
        org.amanzi.neo.loader.core.newsaver.AutoParseTesting.class})
public class SaversSuite {

}