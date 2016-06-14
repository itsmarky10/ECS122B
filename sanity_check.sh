#!/bin/bash
numOfSamp=100
numOfEle=1000

# Compile the java files
javac QuickSelect.java
javac DeterministicSelect.java
javac SelectK.java

# Test
#for((i=0; i<20; i++)); do
#	randomNum=$(od -N4 -tu4 -An /dev/urandom | tr -d " ")
#        randomNum=$((($randomNum % (10**6)) + 1))
#        echo $randomNum
#done
#exit

numOfTestFailed=0
# Generate a file with integers
for ((i=1; i <= numOfSamp; i++)); do
        # Shuf generates a list of integers
	shuf -i 1-$((10**6)) -n $numOfEle > "$i.txt"

        kOrder=$(od -N4 -tu4 -An /dev/urandom | tr -d " ")
        kOrder=$((($kOrder % $numOfEle) + 1))
	java QuickSelect "$i.txt" $kOrder > "QuickSelect_failed_test_$i.txt"
        java DeterministicSelect "$i.txt" $kOrder > "DeterministicSelect_failed_test_$i.txt"
	java SelectK "$i.txt" $kOrder > "SelectK_failed_test_$i.txt"

        diff3 QuickSelect_failed_test_$i.txt DeterministicSelect_failed_test_$i.txt SelectK_failed_test_$i.txt > result.txt
	if [[ -s result.txt ]]; then
		numOfTestFailed=$((numOfTestFailed+1))
	else
		rm "QuickSelect_failed_test_$i.txt"
		rm "DeterministicSelect_failed_test_$i.txt"
                rm "SelectK_failed_test_$i.txt"
	fi

	rm "$i.txt"
done

rm result.txt

if ((numOfTestFailed > 0)); then
	echo "$numOfTestFailed tests failed."
else
	echo "All tests passed."
fi

# Clean up class files
rm ./*.class
