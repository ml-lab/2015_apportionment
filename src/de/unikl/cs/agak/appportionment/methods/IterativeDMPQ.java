/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.unikl.cs.agak.appportionment.methods;

import de.unikl.cs.agak.appportionment.Apportionment;

import java.util.PriorityQueue;
import java.util.Comparator;

public class IterativeDMPQ extends LinearApportionmentMethod {

    public IterativeDMPQ(final double alpha, final double beta) {
        super(alpha, beta);
    }

    @Override
    public Apportionment apportion(final double[] votes, int k) {
        // Initialize heap
        final PriorityQueue<Entry> heap = new PriorityQueue<Entry>(votes.length,
                new Comparator<Entry>() {
                    @Override
                    public int compare(final Entry e1, final Entry e2) {
                        return Double.compare(e1.value, e2.value);
                    }
                });

        // Seed heap with initial values
        for (int i = 0; i < votes.length; i++) {
            heap.add(new Entry(i, d(0) / votes[i]));
        }

        // Subsequently assign seats
        final int[] seats = new int[votes.length];
        while (k > 1) {
            final Entry e = heap.poll();
            final int i = e.index;
            seats[i]++;
            e.value = d(seats[i]) / votes[i];
            heap.add(e);
            k--;
        }

        // Last seat determines astar
        final Entry e = heap.poll();
        seats[e.index]++;

        // Next element determines the last seat
        return new Apportionment(seats, e.value);
    }

    private static class Entry {
        final int index;
        double value;

        Entry(int index, double value) {
            this.index = index;
            this.value = value;
        }
    }
}
