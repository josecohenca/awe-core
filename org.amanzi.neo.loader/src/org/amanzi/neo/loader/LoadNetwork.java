package org.amanzi.neo.loader;

import java.io.File;
import java.io.IOException;

import net.refractions.udig.project.ui.tool.AbstractActionTool;

import org.amanzi.neo.core.NeoCorePlugin;
import org.amanzi.neo.loader.dialogs.TEMSDialog;
import org.amanzi.neo.loader.internal.NeoLoaderPluginMessages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

/**
 * This class launches the network loading, by first asking the user for a file, and then scheduling
 * a job to actually load the file. This class can also be used to support toolbar actions, both
 * standard eclipse actions using a handler like LoadNetworkhandler which specifically calls the
 * run() method to load the network, or through the fact that the run() method is actually an
 * implementation of AbstractActionTool so this can be used as a uDIG specific toolbar action on
 * open maps. To remove this secondary feature, simply remove the 'extends AbstractActionTool' part
 * of the class definition.
 * 
 * @author craig
 * @since 1.0.0
 */
public class LoadNetwork extends AbstractActionTool {
    /*
     * Names of supported files for Network
     */
    public static final String[] NETWORK_FILE_NAMES = {
        "Comma Separated Values Files (*.csv)",
        "Plain Text Files (*.txt)",
        "OpenOffice.org Spreadsheet Files (*.sxc)",
        "Microsoft Excel Spreadsheet Files (*.xls)",
        "All Files (*.*)" };
    
    /*
     * Extensions of supported files for Network
     */
    public static final String[] NETWORK_FILE_EXTENSIONS = {"*.csv", "*.txt", "*.sxc", "*.xls", "*.*"};

    private static String directory = null;

    private final Display display;

	public LoadNetwork() {
        display = null;
	}

    public LoadNetwork(Display display) {
        this.display = display;
    }

	public static String getDirectory(){
		//LN, 9.07.2009, if directory in LoadNetwork is null than get DefaultDirectory from TEMSDialog
		if (directory == null) {
			if (TEMSDialog.hasDefaultDirectory()) {
				directory = TEMSDialog.getDefaultDirectory();
			}
		}
		return directory;
	}
	
	/**
	 * Sets Default Directory path for file dialogs in TEMSLoad and NetworkLoad
	 * 
	 * @param newDirectory new default directory
	 * @author Lagutko_N
	 */
	
	public static void setDirectory(String newDirectory) {
		if (!newDirectory.equals(directory)) {
			directory = newDirectory;		
			TEMSDialog.setDefaultDirectory(newDirectory);
		}
	}
	
	/**
	 * Is DefaultDirectored set
	 * 
	 * @return 
	 * @author Lagutko_N
	 */
	
	public static boolean hasDirectory() {
		return directory != null;
	}

    /**
     * Run from action handler
     */
    public void run() {
        final FileDialog dlg = new FileDialog(display.getActiveShell(), SWT.OPEN);
        dlg.setText(NeoLoaderPluginMessages.NetworkDialog_DialogTitle);
        dlg.setFilterNames(NETWORK_FILE_NAMES);
        dlg.setFilterExtensions(NETWORK_FILE_EXTENSIONS);
        dlg.setFilterPath(getDirectory());
        final String filename = dlg.open();
        if (filename != null) {
            setDirectory(dlg.getFilterPath());
            Job job = new Job("Load Network '" + (new File(filename)).getName() + "'") {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                        NetworkLoader networkLoader;
                        try {
                            networkLoader = new NetworkLoader(filename);
                            networkLoader.run(monitor);
                            networkLoader.printStats(false);
                        } catch (IOException e) {
                            NeoCorePlugin.error("Error loading Network file", e);
                        }
                        return Status.OK_STATUS;
                }
            };
            job.schedule(50);
        }
    }
	public void dispose() {
	}

}
