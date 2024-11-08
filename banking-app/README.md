# Read Me First

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* run mvn clean install
* docker placeholder code for future containerization of the application
* contact @keamp84@gmail.com for support

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

