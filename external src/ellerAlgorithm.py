import random

side = 25
grid_side = side * 2 + 1
path_cells = side * side
grid_cells = grid_side * grid_side

path = [[0 for row in range(side)] for col in range(side)]
grid = [[0 if row % 2 == 1 and col % 2 == 1 else 1 for row in range(grid_side)] for col in range(grid_side)]

def loop_2d_grid(loop, action):
    for row in range(loop):
        for col in range(loop):
            action(row, col)

def display_grid():
    loop_2d_grid(grid_side, lambda row, col: print('#' if grid[row][col] == 1 else '*' if grid[row][col] == 2 else ' ', end=' ') if col < grid_side-1 else print('#' if grid[row][col] == 1 else '*' if grid[row][col] == 2 else ' '))


def display_path():
    loop_2d_grid(side, lambda row, col: print(f'{path[row][col]:3}', end=' ') if col < side-1 else print(f'{path[row][col]:3}'))

def pave_path():
    skip_set = set()
    set_n = 1
    for row in range(side):
        '''cell initialization'''
        for col in range(side):
            if path[row][col] == 0:
                path[row][col] = set_n
                set_n += 1
        
        '''set initialization'''
        set_index = path[row][0]
        for col in range(1, side):
            if random.randint(0, 1) == 1:
                if path[row][col] == path[row][col-1]:
                    skip_set.add((row, col))
                path[row][col] = path[row][col-1]

        '''path sync'''
        if row == side-1:
            for col in range(side):
                if col > 0 and path[row][col] == path[row][col-1]:
                    skip_set.add((row, col))
                path[row][col] = path[row][0]
        if row < side-1:
            '''vertical connection'''
            set_index = path[row][0]
            col_set = []
            for col in range(side):
                if not col_set:
                    set_index = path[row][col]
                if path[row][col] == set_index:
                    col_set.append(col)
                if col + 1 == side or path[row][col+1] != set_index:
                    v_connects = 1 if len(col_set) < 2 else random.randint(1, round(len(col_set)/2))
                    random.shuffle(col_set)
                    for connect in range(v_connects):
                        random_col = col_set[connect]
                        path[row+1][random_col] = path[row][random_col]
                    col_set.clear()

        '''saving horizontal path pavement to grid'''
        grid_row = row*2+1
        for col in range(2, grid_side-2, 2):
            if ((grid_row-1)//2, col//2) in skip_set:
                print(f'({(grid_row-1)//2}, {col//2})')
                continue
            if path[(grid_row-1)//2][(col-2)//2] == path[(grid_row-1)//2][(col)//2]:
                grid[grid_row][col] = 0
        
        if grid_row < grid_side-2:
            '''saving vertical path pavement to  grid'''
            grid_row = row*2+2
            for col in range(1, grid_side-1, 2):
                if path[(grid_row-2)//2][(col-1)//2] == path[(grid_row)//2][(col-1)//2]:
                    grid[grid_row][col] = 0
    
def adjust_wall(row, col):
    if row == grid_side-1 or (grid[row][col] == 1 and grid[row+1][col] == 0):
        grid[row][col] = 2
def main():
    random.seed(42)
    print('path:')
    pave_path()
    loop_2d_grid(grid_side, adjust_wall)
    # display_path()
    with open('./res/maps/test_map.txt', 'w') as f:
        loop_2d_grid(grid_side, lambda row, col: f.write(f'{grid[row][col]} ') if col < grid_side-1 else f.write(f'{grid[row][col]}\n'))
    display_grid()

main()