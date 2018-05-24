package com.roscopeco.moxy.impl.asm;

/*
 * This is used as the key in the stubbed method maps
 * on the mocks.
 */
class StubMethod {
  final String methodName;
  final String methodDesc;
  
  public StubMethod(final String methodName, final String methodDesc) {
    super();
    this.methodName = methodName;
    this.methodDesc = methodDesc;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((methodDesc == null) ? 0 : methodDesc.hashCode());
    result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StubMethod other = (StubMethod) obj;
    if (methodDesc == null) {
      if (other.methodDesc != null)
        return false;
    } else if (!methodDesc.equals(other.methodDesc))
      return false;
    if (methodName == null) {
      if (other.methodName != null)
        return false;
    } else if (!methodName.equals(other.methodName))
      return false;
    return true;
  }
}
