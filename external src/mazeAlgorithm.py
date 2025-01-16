import math
import random
from pathlib import Path

path_length = 25
grid_length = path_length * 2 + 1
grid = [['#' if row % 2 == 0 or col % 2 == 0 else 0 for col in range(grid_length)] for row in range(grid_length)]
sets = {}

def check_set(set_num, row, col):
    if set_num not in sets:
        sets[set_num] = {(row, col)}
    else:
        sets[set_num].add((row, col))
        

def loop(length, action):
    for x in range(length):
        for y in range(length):
            action(x, y)

def display():
    for row in range(grid_length):
        for col in range(grid_length):
            if grid[row][col] != '#': print('  ', end='')
            else: print('# ', end='')
        print()
    
def init_set():

    set_num = 1             
    for row in range(1, grid_length-1, 2):
        '''intialize sets, and randomly joins them'''
        '''set initialization'''                # tracks sets
        for col in range(1, grid_length-1, 2):  # loops every path
            if grid[row][col] == 0:               # checks for unintialized cells
                
                grid[row][col] = set_num          # initializes the path cells
                sets[set_num] = {(row, col)}      # adds the new set with its first cell into the set-dictionary
                set_num += 1                    # iterates the set number

        '''set merging'''
        for col in range(3, grid_length-1, 2):  # iterates the path cells from the second

            last_set = grid[row][col-2]
            current_set = grid[row][col]

            '''check for joint paths'''
            if last_set == current_set: 
                sets[current_set].add((row, col))
                continue    # joint paths are skipped

            '''randomly joins sets except the last row, where every disjoint paths are joint together'''
            if row == grid_length-2 or random.randint(0, 1) == 0:   # randomize chance to either pave the horizontal walls or not
                
                sets[current_set].add((row, col-1))                 # add the wall cell into the current set
                
                for cell in (sets[current_set]):                      # iterate every cell in the current set
                    grid[cell[0]][cell[1]] = last_set               # change every cell in the grid of the current set to the main set
            
                sets[last_set] = sets[last_set].union(sets[current_set])             # union the current set to the main set in the set-dictionary

                del sets[current_set]                               # delete the current set from the set-dicitonary

        if row == grid_length-2: break                              # break out of loop at the last path rows
        '''vertical connection'''
        '''track the current row sets'''
        row_sets = {}                                                                   # tracks current row sets
        for col in range(1, grid_length-1, 2):                                          # iterate path cells
            current_set = grid[row][col]
            if current_set not in row_sets:                                             # check if the set is in the current row sets
                row_sets[current_set] = {col}                                           # if no, add them together with the associated column
            else:
                row_sets[current_set].add(col) 
        '''make at least one connection for each set in this row'''
        for v in row_sets.values():                                                         # iterate every set and their columns
            
            for rand_col in random.sample(list(v), random.randint(1, math.ceil(len(v)/2))): # choose a random amount of columns randomly to make the vertical connection
                current_set = grid[row][rand_col]
                grid[row+2][rand_col] = grid[row+1][rand_col] = current_set         # change the wall and path grid below to match the current set
                sets[current_set] = sets[current_set] | {(row+1, rand_col), (row+2, rand_col)}  # add the vertical connections into the current set in the set-dicitonary

def translate(row, col):
    result = '2' if row == grid_length-1 or (grid[row][col] == '#' and grid[row+1][col] != '#') else '1' if grid[row][col] == '#' else '0'
    result += ' ' if col < grid_length-1 else '\n'
    return result

def main():
    random.seed(42)
    init_set()
    display()
    path = Path(__file__).parent / "../res/maps/test_map.txt"
    with open(path, 'w') as f:
        loop(grid_length, lambda row, col: f.write(translate(row, col)))
        abs_path = Path(f.name).resolve()
        print(f"Absolute Path: {abs_path}")
    
main()