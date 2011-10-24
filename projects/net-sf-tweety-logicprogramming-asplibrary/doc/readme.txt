asp library version 1.0
07/04/2001

* requires javacc. compile the ELPParser.jj to get missing classes.
* full documentation still missing

* known bugs:
  * the dlv solver interface only reads first weight of an answer
    set instead all weights printed.
  
* future improvements:
  * predicate pooling (through something like a signature class)
  * parser improvements (speed / compatibility)
  * better documentation, more examples  
  
* disposed ideas:
  * split a rule into three lists (head, body+, body-), as default
    negation in the head is not supported anyway. the Not class
    would become obsolete anyway, which might cause some work
    on symbolic sets again.
 