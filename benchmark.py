#!/usr/bin/env python

import os

commits = [
    ("c3e3b2c", "Original.txt"),
    ("d5c420b", "IMap_rm_eq.txt"),
    ("08156d3", "IMap_balanceLR.txt"),
    ("ae5c06c", "IMap_latest.txt")
]

# returns 'git checkout _commit_' command
def checkout_command(commit_hash):
   return 'git checkout ' + commit_hash

# take benchmark
def benchmark(commit_hash, commit_title):
	# checkout to a commit
    checkout_command = 'git checkout ' + commit_hash
    os.system(checkout_command)

    # apply stash
    stash_apply_command = 'git stash apply'
    os.system(stash_apply_command)

    # benchmark and output to a particular file
    title = ''.join(commit_title.split(' '))
    # create file name and remove '(' or ')' if it has
    file_name = commit_hash + '_' + title
    file_name = file_name.replace('(', '')
    file_name = file_name.replace(')', '')
    path = './result/' + file_name

    sbt_command = 'sbt -mem 8196 "jmh:run -i 50 -wi 30 -f 1 -t 1 -bm avgt" > ' + path
    os.system(sbt_command)

    # unapply the stash (teardown)
    stash_unapply_command = 'git checkout -- .'
    os.system(stash_unapply_command)

    # rm unnecessary words[]
    sed_command = "sed -i 's/\x1b\[0m//g' " + path
    os.system(sed_command)

for (hsh, title) in commits:
    benchmark(hsh, title)
