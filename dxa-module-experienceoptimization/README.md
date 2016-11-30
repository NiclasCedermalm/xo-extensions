Experience Optimization Module
==================================

This is a DXA module for Experience Optimization (XO) 8.1.1 and DXA 1.5. It is an extension to the standard [DXA Experience Optimization module](https://github.com/sdl/dxa-modules/tree/master/webapp-java/dxa-module-smarttarget-web8) with
the following add-on functionality:

* Make any DXA region XO enabled. This is done in metadata configuration of the page templates.
* All needed XPM markup and content experiments link processing is injected runtime meaning that any DXA view can be used as a SmartTarget promotion.
* If no promotion found for current region it will fall back on the static content already residing on the page

## Setup

1. Install the CMS module as described here: [Installing SDL Web Experience Optimization module in Content Manager](http://docs.sdl.com/LiveContent/content/en-US/SDL%20DXA-v5/GUID-F08C5392-3A96-49DC-9B89-AA1FF368C251) 
2. Setup your webapp with needed configuration (smarttarget_conf.xml, cd_ambient_conf.xml etc). Please refer to the [SDL Web Experience Optimization Web 8 online documentation](http://docs.sdl.com/LiveContent/web/pub.xql?action=home&pub=SDL%20Web%20Experience%20Optimization-v2&lang=en-US) for further information.


## Templating

To create dynamic component presentations to be served by XO you have to add the standard XO TBB 'Add to Experience Optimization' to your component template.

The XO regions are implicit, i.e. no additional templating coding is required to make a specific DXA region to a XO region.
You only have to specify what regions that are managed by SmartTarget in the page template metadata field 'SmartTarget Regions'. In this embedded schema field you can specify the following per region:

* region name
* max items in the region
* allow duplicates between regions

To enable region configuration capabilities you have to use the metadata schema 'SmartTarget Page Template Metadata' instead of the standard DXA one.

See the example page template '/Building Blocks/Modules/SmartTarget/Editor/Templates/SmartTarget Home Page' for a working example.

## Design

The XO module hooks in a custom page builder in the model pipeline. It will populate configured DXA regions with XO content on pages instead of using page component presentations.
If the XO query return an empty list the fallback content is used (the content using templates marked for current region) instead.
What regions that are managed by XO is specified by the the page template metadata field 'smartTargetRegions'.
Only results having component templates defined in the XPM region data are used in the region.
