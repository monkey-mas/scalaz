#!/usr/bin/env python

import sys

def show_benchmark_result(file_path):
  N = 16
  f = open(file_path, 'r')
  last_lines = f.readlines()[-N:]
  for line in last_lines:
    elems = line.split(' ')
    elems_rm_space = [e for e in elems if e != '']
    if len(elems_rm_space) < 5:
      pass
    else:
      avgt = elems_rm_space[4]
      print(avgt)
  f.close()

lists = [
  'Original.txt',
  "IMap_rm_eq.txt",
  "IMap_balanceLR.txt",
  "IMap_latest.txt"
]

for file_name in lists:
  file_path = './result/' + file_name
  print(file_path)
  show_benchmark_result(file_path)
  print('')
