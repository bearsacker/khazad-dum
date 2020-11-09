package com.guillot.moria.ai;

import static com.guillot.moria.dungeon.Tile.NULL;

import java.util.ArrayList;
import java.util.Collections;

import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.utils.Point;


/**
 * A path finder implementation that uses the AStar heuristic based algorithm to determine a path.
 * 
 * @author Kevin Glass
 */
public class AStar {

    private final static int MOVEMENT_COST = 1;

    /** The set of nodes that have been searched through */
    private ArrayList<Node> closed = new ArrayList<>();

    /** The set of nodes that we do not yet consider fully searched */
    private ArrayList<Node> open = new ArrayList<>();

    /** The map being searched */
    private Dungeon map;

    /** The maximum depth of search we're willing to accept before giving up */
    private int maxSearchDistance;

    /** The complete set of nodes across the map */
    private Node[][] nodes;

    /**
     * Create a path finder
     * 
     * @param map The map to be searched
     * @param maxSearchDistance The maximum depth we'll search before giving up
     */
    public AStar(Dungeon map, int maxSearchDistance) {
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;

        this.nodes = new Node[map.getHeight()][map.getWidth()];
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                this.nodes[y][x] = new Node(y, x);
            }
        }
    }

    /**
     * Find path from start to end.
     * 
     * @param start The starting point
     * @param end The ending point
     * @param allowDiag
     * 
     * @return the path or null
     */
    public Path findPath(Point start, Point end, boolean allowDiag) {
        // eastart.y first check, if the destination is blocked, we can't get there
        if (map.getFloor()[end.x][end.y] == null || !(map.getFloor()[end.x][end.y].isFloor || map.getFloor()[end.x][end.y].isStairs)) {
            return null;
        }

        // initial state for A*. The closed group is empend.y. Only the starting
        // tile is in the open list and it'e're already there
        nodes[start.x][start.y].setCost(0);
        nodes[start.x][start.y].setDepth(0);
        closed.clear();
        open.clear();
        open.add(nodes[start.x][start.y]);

        nodes[end.x][end.y].setParent(null);

        // while we haven'n't exceeded our max search depth
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            // pull out the first node in our open list, this is determined to
            // be the most likely to be the next step based on our heuristic
            Node current = getFirstInOpen();
            if (current == nodes[end.x][end.y]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating
            // them as next steps
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    // not a neighbour, its the current tile
                    if ((x == 0) && (y == 0)) {
                        continue;
                    }

                    // if we're not allowing diaganol movement then only
                    // one of x or y can be set
                    if (!allowDiag) {
                        if ((x != 0) && (y != 0)) {
                            continue;
                        }
                    }

                    // determine the location of the neighbour and evaluate it
                    int xp = x + current.getX();
                    int yp = y + current.getY();

                    if (isValidLocation(start.x, start.y, current.getX(), current.getY(), xp, yp)) {
                        // the cost to get to this node is cost the current plus
                        // the movement
                        // cost to reach this node. Note that the heursitic
                        // value is only used
                        // in the sorted open list
                        float nextStepCost = current.getCost() + MOVEMENT_COST;
                        Node neighbour = nodes[xp][yp];

                        // if the new cost we've determined for this node is
                        // lower than
                        // it has been previously makes sure the node hasn'e've
                        // determined that there might have been a better path
                        // to get to
                        // this node so it needs to be re-evaluated
                        if (nextStepCost < neighbour.getCost()) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);
                            }
                        }

                        // if the node hasn't already been processed and
                        // discarded then
                        // reset it's cost to our current cost and add it as a
                        // next possible
                        // step (i.e. to the open list)
                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.setCost(nextStepCost);
                            neighbour.setHeuristic((int) new Point(xp, yp).distanceFrom(new Point(end.x, end.y)));
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }

        // since we'e've run out of search
        // there was no path. Just return null
        if (nodes[end.x][end.y].getParent() == null) {
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent
        // references of the nodes to find out way from the target location back
        // to the start recording the nodes on the way.
        Path path = new Path();
        Node target = nodes[end.x][end.y];
        while (target != nodes[start.x][start.y]) {
            path.prependStep(target.getX(), target.getY());
            target = target.getParent();
        }
        path.prependStep(start.x, start.y);

        return path;
    }

    /**
     * Get the first element from the open list. This is the next one to be searched.
     * 
     * @return The first element in the open list
     */
    protected Node getFirstInOpen() {
        return open.get(0);
    }

    /**
     * Add a node to the open list
     * 
     * @param node The node to be added to the open list
     */
    protected void addToOpen(Node node) {
        open.add(node);
        Collections.sort(open);
    }

    /**
     * Check if a node is in the open list
     * 
     * @param node The node to check for
     * @return True if the node given is in the open list
     */
    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
     * Remove a node from the open list
     * 
     * @param node The node to remove from the open list
     */
    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
     * Add a node to the closed list
     * 
     * @param node The node to add to the closed list
     */
    protected void addToClosed(Node node) {
        closed.add(node);
    }

    /**
     * Check if the node supplied is in the closed list
     * 
     * @param node The node to search for
     * @return True if the node specified is in the closed list
     */
    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
     * Remove a node from the closed list
     * 
     * @param node The node to remove from the closed list
     */
    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    /**
     * Check if a given location is valid for the supplied mover
     * 
     * @param mover The mover that would hold a given location
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param cx The current x coordinate
     * @param cy The current y coordinate
     * @param x The x coordinate of the location to check
     * @param y The y coordinate of the location to check
     * @return True if the location is valid for the given mover
     */
    protected boolean isValidLocation(int sx, int sy, int cx, int cy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getHeight()) || (y >= map.getWidth());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.getFloor()[x][y] == NULL || !(map.getFloor()[x][y].isFloor || map.getFloor()[x][y].isStairs);
        }

        return !invalid;
    }
}
