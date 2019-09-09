#!/usr/bin/python

import os

def exec_cmd(cmd):
	os.system(cmd)

def clean() :
	exec_cmd("rm -rf com/github/kari/*.class")

def build() :
	exec_cmd("javac com/github/kari/*.java")

def execute() :
	exec_cmd("java com.github.kari.Haha 1.hprof")

if __name__ == "__main__":
	clean()
	build()
	execute()