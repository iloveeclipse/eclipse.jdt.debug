/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.debug.tests.performance;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.tests.AbstractDebugTest;

/**
 * Tests performance of conditional breakpoints.
 */
public class PerfConditionalBreakpointsTests extends AbstractDebugTest {
	
	public PerfConditionalBreakpointsTests(String name) {
		super(name);
	}

	public void testConditionalBreakpoint() throws Exception {
		String typeName = "PerfLoop";
		IJavaLineBreakpoint bp = createConditionalLineBreakpoint(22, typeName, "i == 99", true);
		
		IJavaThread thread= null;
		try {
			thread= launchToLineBreakpoint(typeName, bp);

			IJavaStackFrame frame = (IJavaStackFrame)thread.getTopStackFrame();
			IVariable var = frame.findVariable("i");
			assertNotNull("Could not find variable 'i'", var);
			
			IJavaPrimitiveValue value = (IJavaPrimitiveValue)var.getValue();
			assertNotNull("variable 'i' has no value", value);
			int iValue = value.getIntValue();
			assertEquals("value of 'i' incorrect", 99, iValue);
			
			bp.delete();
		} finally {
			terminateAndRemove(thread);
			removeAllBreakpoints();
		}		
	}
}
