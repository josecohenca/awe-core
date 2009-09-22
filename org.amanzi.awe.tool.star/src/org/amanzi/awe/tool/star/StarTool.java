/**
 * 
 */
package org.amanzi.awe.tool.star;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.refractions.udig.catalog.CatalogPlugin;
import net.refractions.udig.catalog.ICatalog;
import net.refractions.udig.catalog.IResolve;
import net.refractions.udig.core.Pair;
import net.refractions.udig.mapgraphic.internal.MapGraphicResource;
import net.refractions.udig.mapgraphic.internal.MapGraphicService;
import net.refractions.udig.project.IBlackboard;
import net.refractions.udig.project.ILayer;
import net.refractions.udig.project.IMap;
import net.refractions.udig.project.command.Command;
import net.refractions.udig.project.command.NavCommand;
import net.refractions.udig.project.internal.render.ViewportModel;
import net.refractions.udig.project.ui.ApplicationGIS;
import net.refractions.udig.project.ui.internal.commands.draw.TranslateCommand;
import net.refractions.udig.project.ui.render.displayAdapter.MapMouseEvent;
import net.refractions.udig.project.ui.render.displayAdapter.ViewportPane;
import net.refractions.udig.project.ui.tool.AbstractModalTool;

import org.amanzi.awe.mapgraphic.star.StarMapGraphic;
import org.eclipse.core.runtime.IProgressMonitor;
import org.neo4j.api.core.Node;

/**
 * <p>
 * Star analysing tool
 * </p>
 * 
 * @author Cinkel_A //most code are depends on
 * @since 1.1.0
 */
public class StarTool extends AbstractModalTool {
    private boolean dragging=false;
    private Point start=null;

    private TranslateCommand command;
    /**
     * Creates an new instance of Pan
     */
    public StarTool() {
        super(MOUSE | MOTION);
    }

    /**
     * @see net.refractions.udig.project.ui.tool.AbstractTool#mouseDragged(net.refractions.udig.project.render.displayAdapter.MapMouseEvent)
     */
    public void mouseDragged(MapMouseEvent e) {
        if (dragging) {
            command.setTranslation(e.x- start.x, e.y - start.y);
            context.getViewportPane().repaint();
        }
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        // add layer on map if necessary
        if (active) {
            IMap map = getContext().getMap();
            List<ILayer> layers = map.getMapLayers();
            for (ILayer layer : layers) {
                if (layer.getGeoResource().canResolve(StarMapGraphic.class)) {
                    return;
                }
            }
            ICatalog catalog = CatalogPlugin.getDefault().getLocalCatalog();
            List<IResolve> serv = catalog.find(MapGraphicService.SERVICE_URL, null);
            try {
                for (IResolve iResolve : serv) {
                    List<MapGraphicResource> resources;
                    resources = ((MapGraphicService)iResolve).resources(null);

                for (MapGraphicResource mapGraphicResource : resources) {
                        if (mapGraphicResource.canResolve(StarMapGraphic.class)) {
                            List list = new ArrayList();
                            list.add(mapGraphicResource);
                            ApplicationGIS.addLayersToMap(map, list, map.getMapLayers().size());
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                throw (RuntimeException)new RuntimeException().initCause(e);
            }
        }
    }
    /**
     * @see net.refractions.udig.project.ui.tool.AbstractTool#mousePressed(net.refractions.udig.project.render.displayAdapter.MapMouseEvent)
     */
    public void mousePressed(MapMouseEvent e) {
    	
        if (validModifierButtonCombo(e)) {
        	((ViewportPane)context.getMapDisplay()).enableDrawCommands(false);
            dragging = true;
            start = e.getPoint();
            command=context.getDrawFactory().createTranslateCommand(0,0);
            context.sendASyncCommand(command);
        }
    }

    /**
     * Returns true if the combination of buttons and modifiers are legal to execute the pan.
     * <p>
     * This version returns true if button 1 is down and no modifiers
     * </p>
     * @param e
     * @return
     */
	protected boolean validModifierButtonCombo(MapMouseEvent e) {
		return e.buttons== MapMouseEvent.BUTTON1
                && !(e.modifiersDown());
	}

    /**
     * @see net.refractions.udig.project.ui.tool.AbstractTool#mouseReleased(net.refractions.udig.project.render.displayAdapter.MapMouseEvent)
     */
    public void mouseReleased(MapMouseEvent e) {
        if (dragging) {
        	((ViewportPane)context.getMapDisplay()).enableDrawCommands(true);
            Point end=e.getPoint();
            NavCommand finalPan = context.getNavigationFactory().createPanCommandUsingScreenCoords(start.x-end.x, start.y-end.y);
            context.sendASyncCommand(new PanAndInvalidate(finalPan, command));

            dragging = false;

        }else{
            final IMap map = getContext().getMap();
            IBlackboard blackboard = map.getBlackboard();
            Map<Node, java.awt.Point> nodesMap = (Map<Node, java.awt.Point>)blackboard.get(StarMapGraphic.BLACKBOARD_NODE_LIST);
            if (nodesMap == null) {
                return;
            }
            Pair<Point, Node> pair = StarMapGraphic.getSector(e.getPoint(), nodesMap);
            blackboard.put(StarMapGraphic.BLACKBOARD_START_ANALYSER, pair);
            updateStarLayer();
        }
    }
    /**
     * @see net.refractions.udig.project.ui.tool.Tool#dispose()
     */
    public void dispose() {
        super.dispose();
    }

    @Override
    public void mouseMoved(MapMouseEvent e) {
        super.mouseMoved(e);
        IMap map = getContext().getMap();
        if (!dragging) {
            map.getBlackboard().put(StarMapGraphic.BLACKBOARD_CENTER_POINT, e.getPoint());
            updateStarLayer();
        } else {
            map.getBlackboard().put(StarMapGraphic.BLACKBOARD_CENTER_POINT, null);
        }
    }

    /**
     * updates star layer
     */
    private void updateStarLayer() {
        IMap map = getContext().getMap();
        List<ILayer> layers = map.getMapLayers();
        for (ILayer layer : layers) {
            if (layer.getGeoResource().canResolve(StarMapGraphic.class)) {
                layer.refresh(null);
                return;
            }
        }

    }

    /**
     * Executes the specified pan command, and only after it is executed, expires the last translate
     * command
     */
    private class PanAndInvalidate implements Command, NavCommand {

        private NavCommand command;
        private TranslateCommand expire;

        PanAndInvalidate(NavCommand command, TranslateCommand expire) {
            this.command = command;
            this.expire = expire;
        }

        public Command copy() {
            return new PanAndInvalidate(command, expire);
        }

        public String getName() {
            return "PanAndDiscard";
        }

        public void run( IProgressMonitor monitor ) throws Exception {
            try {
                command.run(monitor);
            } finally {
                expire.setValid(false);
            }
        }

        public void setViewportModel( ViewportModel model ) {
            command.setViewportModel(model);
        }

        public net.refractions.udig.project.internal.Map getMap() {
            return command.getMap();
        }

        public void setMap( IMap map ) {
            command.setMap(map);
        }

        public void rollback( IProgressMonitor monitor ) throws Exception {
            command.rollback(monitor);
        }

    }
}