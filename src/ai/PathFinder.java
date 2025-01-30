package ai;

import java.util.ArrayList;

import entity.Entity;
import main.GamePanel;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int steps = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        
    }

    public void instantiateNodes() {

        node = new Node[gp.mapWorldCol][gp.mapWorldRow];

        int col = 0, row = 0;

        while (col < gp.mapWorldCol && row < gp.mapWorldRow) {

            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.mapWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {

        int col = 0, row = 0;

        while (col < gp.mapWorldCol && row < gp.mapWorldRow) {

            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;
            
            col++;
            if (col == gp.mapWorldCol) {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        steps = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {

        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];

        int col = 0, row = 0;

        while (col < gp.mapWorldCol && row < gp.mapWorldRow) {

            int tileNum = gp.tileM.mapTileNum[col][row];
            node[col][row].solid = gp.tileM.tile[tileNum].collision;

            getCost(node[col][row]);
            
            col++;
            if (col == gp.mapWorldCol) {
                col = 0;
                row++;
            }
        }   
    }

    public void getCost(Node node) {

        int x = Math.abs(node.col - startNode.col);
        int y = Math.abs(node.row - startNode.row);
        node.gCost = x + y;

        x = Math.abs(node.col - goalNode.col);
        y = Math.abs(node.row - goalNode.row);
        node.hCost = x + y;

        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while (!goalReached && steps < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            if (row - 1 >= 0)
                openNode(node[col][row-1]);
            if (col - 1 >= 0)
                openNode(node[col-1][row]);
            if (row + 1 < gp.mapWorldRow)
                openNode(node[col][row+1]);
            if (col + 1 < gp.mapWorldCol)
                openNode(node[col+1][row]);
       

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }

                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if (openList.isEmpty())
                break;

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }
            steps++;
        }
        return goalReached;
    }

    public void openNode(Node node) {

        if (!node.open && !node.checked && !node.solid) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackPath() {
        Node current = goalNode;

        while (current != startNode) {

            pathList.add(0, current);
            current = current.parent;
        }
    }

}
