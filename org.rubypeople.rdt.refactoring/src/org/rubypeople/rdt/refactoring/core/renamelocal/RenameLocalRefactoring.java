/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2006 Mirko Stocker <me@misto.ch>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/

package org.rubypeople.rdt.refactoring.core.renamelocal;

import org.rubypeople.rdt.refactoring.core.IRefactoringContext;
import org.rubypeople.rdt.refactoring.core.RubyRefactoring;
import org.rubypeople.rdt.refactoring.documentprovider.DocumentProvider;
import org.rubypeople.rdt.refactoring.ui.pages.RenamePage;

public class RenameLocalRefactoring extends RubyRefactoring {

	public static final String NAME = Messages.RenameLocalRefactoring_Name;
	
	public RenameLocalRefactoring(IRefactoringContext selectionProvider) {
		super(NAME, selectionProvider);
		
		DocumentProvider docProvider = getDocumentProvider();
		
		RenameLocalConfig config = new RenameLocalConfig(docProvider, selectionProvider.getCaretPosition());
		RenameLocalConditionChecker checker = new RenameLocalConditionChecker(config);
		setRefactoringConditionChecker(checker);
		
		if(checker.shouldPerform()) {
			RenameLocalEditProvider editProvider = new RenameLocalEditProvider(config);
			setEditProvider(editProvider);

			String name = config.getSelectedNodeName();
			editProvider.setSelectedVariableName(name);
			editProvider.setNewVariableName(name);
			
			VariableNameProvider nameProvider = new VariableNameProvider(name);
			pages.add(new RenamePage(name, nameProvider));
			nameProvider.addObserver(editProvider);
		}
	}
}
