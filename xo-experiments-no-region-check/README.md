XO Experiments No Region Check 
=================================

This is a little extension to get around the validation check in the XO GUI when creating multiple experiments on the same page and region.

This is done by renaming the returned region (set all characters to lowercase) when listing experiments. By doing that
we can bypass the validation logic in the XO GUI. This is a temporary workaround until this is part of the standard product.

Setup
--------

1. Compile the project into a jar: `mvn package`
2. Drop that into your XO management micro service installation: services/management
3. Make a backup of the JAR file services/management/xo-management-extension-8.1.1.jar
4. Patch the JAR by doing the following: zip -d xo-management-extension-8.1.1.jar com/sdl/web/experience/management/experiments/ExperimentEntity.class
5. Restart XO service

