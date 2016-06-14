#!/bin/bash

numOfEle=0
if [ $1 = 2 ]; then
	numOfEle=$((10**2))
elif [ $1 = 3 ]; then
	numOfEle=$((10**3))
elif [ $1 = 4 ]; then
	numOfEle=$((10**4))
elif [ $1 = 5 ]; then
	numOfEle=$((10**5))
elif [ $1 = 6 ]; then
	numOfEle=$((10**6))
else
	echo "Error: 10^$1 is not part of this project"
	exit
fi

csvFilename="gatheredDataPow"$1".csv"

echo -n "\"sample Number\",\"QuickSelect\",\"DeterministicSelect\",\"SelectK_InsertSort\"" > $csvFilename

javac QuickSelect_timed.java
javac DeterministicSelect_timed.java
javac SelectK_timed.java

#Test
#num=20
#for ((i=1; i <= num; i++)); do
#	echo $(od -N 4 -t uL -An /dev/urandom | tr -d " ")
#done
#exit

for ((i=1; i <= 200; i++)); do
        # Shuf generates a list of integers
	shuf -i 1-$((10**6)) -n $numOfEle > "$i.txt"

        echo "" >> $csvFilename

        # Store sample number in csv
        echo -n "$i," >> $csvFilename
        TIMEFORMAT=%R

	kOrder=$(od -N4 -tu4 -An /dev/urandom | tr -d " ")
	kOrder=$((($kOrder % numOfEle) + 1))

        # Get run time of each algorithm
        echo -n $({ time java QuickSelect_timed $i".txt" $kOrder ; } 2>&1 | tr -d '\n' ) >> $csvFilename
        echo -n "," >> $csvFilename
        echo -n $({ time java DeterministicSelect_timed $i".txt" $kOrder ; } 2>&1 | tr -d '\n' ) >> $csvFilename
	echo -n "," >> $csvFilename
        echo -n $({ time java SelectK_timed $i".txt" $kOrder ; } 2>&1 | tr -d '\n' ) >> $csvFilename

        rm "$i.txt"
done

rm ./*.class
