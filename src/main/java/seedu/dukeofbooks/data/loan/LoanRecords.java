package seedu.dukeofbooks.data.loan;

import seedu.dukeofbooks.data.person.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import seedu.dukeofbooks.data.book.BorrowableItem;

public class LoanRecords {
    private static List<Loan> internalList;

    public LoanRecords() {
        internalList = new ArrayList<>();
    }

    public int size() {
        return internalList.size();
    }

    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    public boolean contains(Object o) {
        if (!(o instanceof Loan)) {
            return false;
        }
        return internalList.contains(o);
    }

    public List<Loan> findByItem(BorrowableItem toFind) {
        List<Loan> res = new ArrayList<>();
        for (Loan loan : internalList) {
            if (loan.getBorrowedItem().equals(toFind)) {
                res.add(loan);
            }
        }
        return res;
    }

    public List<Loan> findByPerson(Person toFind) {
        List<Loan> res = new ArrayList<>();
        for (Loan loan : internalList) {
            if (loan.getBorrower().equals(toFind)) {
                res.add(loan);
            }
        }
        return res;
    }

    public List<Loan> findByPersonItem(Person person, BorrowableItem item) {
        List<Loan> res = new ArrayList<>();
        for (Loan loan : internalList) {
            if (loan.getBorrower().equals(person)
                    && loan.getBorrowedItem().equals(item)) {
                res.add(loan);
            }
        }
        return res;
    }

    public Loan getLastActiveLoan(Person person) {
        ListIterator<Loan> it = internalList.listIterator(internalList.size());
        while (it.hasPrevious()) {
            Loan loan = it.previous();
            if (loan.getBorrower().equals(person) && !loan.isReturned()) {
                return loan;
            }
        }
        return null;
    }

    public static Loan getLastActiveLoan(BorrowableItem item) {
        ListIterator<Loan> it = internalList.listIterator(internalList.size());
        while (it.hasPrevious()) {
            Loan loan = it.previous();
            if (loan.getBorrowedItem().equals(item) && !loan.isReturned()) {
                return loan;
            }
        }
        return null;

    }

    public Loan getLastActiveLoan(Person person, BorrowableItem item) {
        ListIterator<Loan> it = internalList.listIterator(internalList.size());
        while (it.hasPrevious()) {
            Loan loan = it.previous();
            if (loan.getBorrowedItem().equals(item)
                    && loan.getBorrower().equals(person)
                    && !loan.isReturned()) {
                return loan;
            }
        }
        return null;
    }

    public List<Loan> getInternalList() {
        return internalList;
    }

    public Iterator<Loan> iterator() {
        return internalList.iterator();
    }

    public Object[] toArray() {
        return internalList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return internalList.toArray(a);
    }

    public boolean add(Loan toAdd) {
        return internalList.add(toAdd);
    }

    public boolean removeItem(Loan toRemove) {
        return internalList.remove(toRemove);
    }

    public void clear() {
        internalList.clear();
    }

    public Loan get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= internalList.size()) {
            throw new IndexOutOfBoundsException();
        }
        return internalList.get(index);
    }

    public Loan removeIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= internalList.size()) {
            throw new IndexOutOfBoundsException();
        }
        return internalList.remove(index);
    }

    public int indexOf(Loan toFind) {
        return internalList.indexOf(toFind);
    }

    public int lastIndexOf(Loan toFind) {
        return internalList.lastIndexOf(toFind);
    }

    public ListIterator<Loan> listIterator() {
        return internalList.listIterator();
    }

    public ListIterator<Loan> listIterator(int index) {
        return internalList.listIterator(index);
    }

    public List<Loan> subList(int fromIndex, int toIndex) {
        return internalList.subList(fromIndex, toIndex);
    }
}
