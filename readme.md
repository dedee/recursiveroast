

# Wopper3

(C)opyright D.Pfeifle 2010-10-21

See http://de.wikipedia.org/wiki/Fraktal for details.
The L-System is described here in more detail:

Example:

(1) winkel = 36
(2) R = F
(3) 0 : BASE = R++R++R++R++R
(4) 1 : R = R++R++R|R-R++R

(1) definition of the angle in degrees
(2) constant definition, any char is defined as a command
(3) recursion level 0, always BASE is defined as base pattern
(4) next recursion level, 0..n replacement definitions per level

Commands:

```
F  Draw
f  Move
+  Turn right
-  Turn left
   |  Turn 180°
   [  Push pos to stack
   ]  Pop pos from stack
```


