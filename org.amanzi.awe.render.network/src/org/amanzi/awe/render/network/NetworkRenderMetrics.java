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
package org.amanzi.awe.render.network;

import java.util.HashSet;
import java.util.Set;

import javax.media.jai.util.Range;

import net.refractions.udig.project.ILayer;
import net.refractions.udig.project.internal.render.Renderer;
import net.refractions.udig.project.render.AbstractRenderMetrics;
import net.refractions.udig.project.render.IRenderContext;
import net.refractions.udig.project.render.IRenderMetricsFactory;

import org.amanzi.awe.neostyle.NeoStyleContent;

public class NetworkRenderMetrics extends AbstractRenderMetrics {

	/**
     * Construct based on context (which includes map, layer and geoResource).
     * @param context
     * @param factory
     */
    public NetworkRenderMetrics( IRenderContext context, IRenderMetricsFactory factory ) {
        super(context, factory);
    }

    /**
     * We only support rendering a single layer
     * @return false
     */
    public boolean canAddLayer( ILayer layer ) {
        return false;
    }

    /**
     * We cannot use styles, this is a raw Java2D renderer
     */
    public boolean canStyle( String styleID, Object value ) {
        return NeoStyleContent.ID.equals(styleID);
    }

    /**
     * @return a new SitesRenderer
     */
    public Renderer createRenderer() {
        return new NetworkRenderer();
    }

    /**
     * @return a new empty HashSet<Range>
     */
    public Set<Range> getValidScaleRanges() {
        return new HashSet<Range>();
    }
}
