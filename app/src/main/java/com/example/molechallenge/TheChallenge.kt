package com.example.molechallenge

import java.util.*


var moleCount = 0
var mazeList = mutableListOf<Coordinates>()
var lastUnknownCount = 0
var globalAreaFilled = false

fun main(){

    val input = Scanner(System.`in`)

    // putting everything in a list for easier handling
    for (lineNum in 0 until 16) {
        val line = input.nextLine()
        val iter = line.length
        for (charNum in 0 until iter) {
            val stringName = when (line[charNum].toString()) {
                "|", "-" -> "fence"
                "+" -> "fencepost"
                " " -> "garden"
                "o" -> "mole"
                else -> "outside"
            }
            mazeList.add(Coordinates(lineNum, charNum, stringName, "unknown"))
        }
    }

    // determining inside and outside
    // check borders
    mazeList.forEach() {
        // everything not fence is outside, fence is garden
        if ((it.xValue == 0 || it.xValue == 15 || it.yValue == 0 || it.yValue == 15) && it.stringName == "fence") {
            it.inside = "inside"
            it.stringName =
                "garden" // easy fix to determine fields bordering the outside of the field
        } else if (it.xValue == 0 || it.xValue == 15 || it.yValue == 0 || it.yValue == 15) {
            it.inside = "outside"
        }
    }

    // check neighbors of bordering fields
    outsideInside()

    // determining mole count
    mazeList.forEach {
        if (it.inside == "inside" && it.stringName == "mole") {
            moleCount++
            // checking coordinates of all moles to double check with exercise
            // println(it.xValue.toString() + "," + it.yValue.toString())
        }
    }

    // printing out results
    println(moleCount.toString())
}

fun outsideInside() {
    mazeList.forEach {
        // only checking for fields not yet assigned inside or outside
        if (it.inside == "unknown") {
            for (compare in mazeList) {
                // checking neighboring fields and assigning the same value or a different value (fence)
                // double fence and fencepost need some special attention
                // stop at fields only touching fences and fenceposts
                if (compare.xValue == it.xValue && (compare.yValue == it.yValue + 1 || compare.yValue == it.yValue - 1)
                    && compare.inside != "unknown" && ((compare.stringName != "fence" && compare.stringName != "fencepost") || globalAreaFilled)
                ) {
                    assignValues(it, compare)
                } else if (compare.yValue == it.yValue && (compare.xValue == it.xValue + 1 || compare.xValue == it.xValue - 1)
                    && compare.inside != "unknown" && ((compare.stringName != "fence" && compare.stringName != "fencepost") || globalAreaFilled)
                ) {
                    assignValues(it, compare)
                }
            }
        }
    }
    // checking if unknown fields are left
    var unknownCount = 0
    mazeList.forEach {
        if (it.inside == "unknown") unknownCount++
    }

    // rerun fun until all unknown fields have been determined that dont border fences or fenceposts
    // if all fields only touch fences or fenceposts, assign global to continue assignment on other side of fences/fenceposts
    if (unknownCount > 0) {
        if (unknownCount != lastUnknownCount) {
            lastUnknownCount = unknownCount
            outsideInside()
        } else {
            globalAreaFilled = true
            lastUnknownCount = 0
            outsideInside()
        }
    }
}

fun assignValues(it: Coordinates, compare: Coordinates) {
    globalAreaFilled = false
    if (compare.stringName == "fence") {
        if (it.stringName == "fencepost" || it.stringName == "fence") it.inside = compare.inside
        else {
            if (compare.inside == "inside") it.inside = "outside"
            else it.inside = "inside"
        }
    } else if (compare.stringName == "fencepost") {
        if (it.stringName == "fencepost" || it.stringName == "fence") it.inside = compare.inside
        else {
            if (compare.inside == "inside") it.inside = "outside"
            else it.inside = "inside"
        }
    } else {
        it.inside = compare.inside
    }
}

// class to see coordinates and other status
class Coordinates(var xValue: Int, var yValue: Int, var stringName: String, var inside: String)

// earlier attempts for starting levels
/*
// determining moles inside the garden
mazeList.forEach(){
    if (it.stringName == "mole"){
        for (compare in mazeList){
            if (compare.xValue == it.xValue && (compare.yValue == it.yValue + 1 || compare.yValue == it.yValue - 1)
                && compare.stringName == "garden" && !moleList.contains(it)){
                moleList.add(it)
                newMoleList.add(it)
            }else if (compare.yValue == it.yValue && (compare.xValue == it.xValue + 1 || compare.xValue == it.xValue - 1)
                && compare.stringName == "garden" && !moleList.contains(it)){
                moleList.add(it)
                newMoleList.add(it)
            }
        }
    }
}

// checking for moles next to moles
fun moleNextToMole() {
    if (newMoleList.isNotEmpty()) {
        newMoleList.forEach() {
            for (compare in mazeList) {
                if (compare.xValue == it.xValue && (compare.yValue == it.yValue + 1 || compare.yValue == it.yValue - 1)
                    && compare.stringName == "mole" && !moleList.contains(compare) && newMoleList.contains(it)) {
                    moleList.add(compare)
                    if (!newMoleListAdd.contains(compare)) newMoleListAdd.add(compare)
                } else if (compare.yValue == it.yValue && (compare.xValue == it.xValue + 1 || compare.xValue == it.xValue - 1)
                    && compare.stringName == "mole" && !moleList.contains(compare) && newMoleList.contains(it)) {
                    moleList.add(compare)
                    if (!newMoleListAdd.contains(compare)) newMoleListAdd.add(compare)
                }
            }
            if (!newMoleListRemove.contains(it)) newMoleListRemove.add(it)
        }

        // remove and add elements from/to lists
        if (newMoleListRemove.isNotEmpty()) {
            newMoleList.removeAll(newMoleListRemove)
            newMoleListRemove.clear()
        }
        if (newMoleListAdd.isNotEmpty()) {
            newMoleList.addAll(newMoleListAdd)
            newMoleListAdd.clear()
        }

        // rerun fun until all neighboring moles have been found
        if (newMoleList.isNotEmpty()) {
            moleNextToMole()
        }

    }
}

// marking border fences connected to other fields as outside
           for (compare in mazeList) {
            if ((it.xValue == 0 || it.xValue == 15) && (compare.yValue == it.yValue + 1 || compare.yValue == it.yValue - 1) &&
                it.stringName == "fence" && compare.stringName != "fence"){
                it.inside = "outside"
            } else if ((it.yValue == 0 || it.yValue == 15) && (compare.xValue == it.xValue + 1 || compare.xValue == it.xValue - 1) &&
                it.stringName == "fence" && compare.stringName != "fence"){
                it.inside = "outside"
            }
        }

 */
