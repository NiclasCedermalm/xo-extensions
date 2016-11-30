XO Analytics Provider Framework
============================================

Is a simple framework for building XO analytics providers. Comes with a handy base class for analytics managers, pluggable winner algorithms etc.

The following algorithms are provided through the framework;

* Configurable Chi Square - A configurable version of standard Chi Square algorithm where minimum conversions per variant can be configured (property name: MinimumConversionsPerVariant)
* Reach Conversion Goal Algorithm - Simple algorithm that just make the variant that first reach a specific number of conversions to winner (property name: ConversionGoal ) 


Format of the configuration in smarttarget_conf.xml:

```
<Analytics implementationClass="[Analytics provider class name]" timeoutMilliseconds="5000" trackingRedirectUrl="/redirect/">
    [Properties to the analytics provider ...]
    
    ExperimentWinnerAlgorithmClassName>[Experiment winner class name]</ExperimentWinnerAlgorithmClassName>
    [Properties to the algorithm...]
   
</Analytics>
```
