package core;

import util.*;

/** Variable is created lua name scopes, and is a named, lua variable that
 * either refers to a lua local, global, or upvalue storage location.  
 */
public class Variavel {
	
	/** The name as it appears in lua source code */
	public final String name;
	
	/** The lua scope in which this variable is defined. */ 
	public final NameScope definingScope;
	
	/** true if this variable is an upvalue */
	public boolean isupvalue;
	
	/** true if there are assignments made to this variable */
	public boolean hasassignments;

	/** When hasassignments == false, and the initial value is a constant, this is the initial value */
	public LuaValue initialValue;
	
	/** Global is named variable not associated with a defining scope */
	public Variavel(String name) {
		this.name = name;
		this.definingScope = null;
	}
	public Variavel(String name, NameScope definingScope) {
	/** Local variable is defined in a particular scope.  */
		this.name = name;
		this.definingScope = definingScope;
	}
	public boolean isLocal() {
		return this.definingScope != null;
	}
	public boolean isConstant() {
		return ! hasassignments && initialValue != null;
	}
}