REM Argment is either Method1, Method2 or Method3
echo ""
echo "Author: Anika Raghuvanshi"
echo "This Batch file invokes Decision Function program with different arguments
echo "Following are some expressions containing Positive Literals only"
echo  ""

java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(C)
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(A+C)
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(A+C)(C+E)
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(A+C)(C+E)
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(A+E)(B+C)(B+E) 
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(C+D+E)(A+C+E)(C+B)
java -cp build\classes DecisionFunction.DecisionFunction %1 (A)(A+E)(B)(B+E)(C)(C+E)(D)(D+E)

echo ""
echo "Following examples use expressions containing Negative Literals "
echo "Negative literals are denoted by lowercase letters"
echo ""

java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(a+C) 
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B)(A+E)(b+C)(B+E) 
java -cp build\classes DecisionFunction.DecisionFunction %1 (A+B+C)(c+D+E)(b+K+F)(a+G+H)

echo ""
echo "Following examples are trivial examples showing syntax of the arguments"
echo "Expressions can have whitespaces. Expression must then be within quotes"
echo ""

java -cp build\classes DecisionFunction.DecisionFunction %1 A 
java -cp build\classes DecisionFunction.DecisionFunction %1 AG 
java -cp build\classes DecisionFunction.DecisionFunction %1 (A) 
java -cp build\classes DecisionFunction.DecisionFunction %1 (AG) 
java -cp build\classes DecisionFunction.DecisionFunction %1 (A)(G) 
java -cp build\classes DecisionFunction.DecisionFunction %1 (A)(a) 
java -cp build\classes DecisionFunction.DecisionFunction %1 "(A+B) (C)" 
java -cp build\classes DecisionFunction.DecisionFunction %1 " ( A + B) (A+C ) " 
