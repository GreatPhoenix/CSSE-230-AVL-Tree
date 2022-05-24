import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;



public class AVLTree<T extends Comparable<? super T>> implements Iterable<T> {
	public BinaryNode root;
	private int size;
	private int numberOfRotations;
	

	public AVLTree() {
		root = null;
		this.size = 0;
	}

	public AVLTree(BinaryNode n) {
		root = n;
		this.size = 1;
	}

	public boolean insert(T value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		if (this.size == 0) {
			this.root = new BinaryNode(value);
			this.size += 1;
			return true;
		}
		if (value.compareTo(root.element) == 1) {
			if (root.rightChild == null) {
				root.rightChild = (new BinaryNode(value));
				this.size += 1;
				return true;
			} else {
				
				return insertHelper(root, value);
			}
		} else if (value.compareTo(root.element) == -1) {
			if (root.leftChild == null) {
				root.leftChild = (new BinaryNode(value));
				this.size += 1;
				return true;
			} else {
				
				return insertHelper(root, value);
			}

		}
		return false;
	}

	private boolean insertHelper(BinaryNode currentNode, T value) {
		if (value.compareTo(currentNode.element) == -1) {
			if (currentNode.leftChild == null) {
				currentNode.leftChild = (new BinaryNode(value));
				this.size += 1;
				return true;
			} else {
				insertHelper(currentNode.leftChild, value);
				return BalencedHandler(currentNode);

			}
		}

		else if (value.compareTo(currentNode.element) == 1) {
			if (currentNode.rightChild == null) {
				currentNode.rightChild = new BinaryNode(value);
				BalencedHandler(currentNode);
				this.size += 1;
				return true;
			}

			else {

				insertHelper(currentNode.rightChild, value);
				return BalencedHandler(currentNode);
			}

		}
		return false;

	}

	private void rightRotationHandler(BinaryNode currentNode) {
		BinaryNode savedRightLeftChild = null;
		BinaryNode heldRightChild = currentNode.rightChild;
		BinaryNode heldLeftChild = currentNode.leftChild;
		T savedElement = currentNode.element;
		
		if (currentNode.rightChild.leftChild != null && currentNode.rightChild.rightChild == null){
			
			
			currentNode.rightChild.rightChild = new BinaryNode(currentNode.rightChild.leftChild.element);
			currentNode.rightChild.rightChild.element = currentNode.rightChild.element;
			currentNode.rightChild.element = currentNode.rightChild.leftChild.element;
			
			currentNode.rightChild.leftChild = null;
			this.numberOfRotations += 1;
			
			heldRightChild = currentNode.rightChild;
			heldLeftChild = currentNode.leftChild;
			savedElement = currentNode.element;
		}

		if (currentNode.rightChild.leftChild != null) {
			savedRightLeftChild = currentNode.rightChild.leftChild;
			
		} 
		currentNode.element = currentNode.rightChild.element;
		currentNode.rightChild = currentNode.rightChild.rightChild;
		currentNode.leftChild = new BinaryNode(savedElement);
		currentNode.leftChild.leftChild = heldLeftChild;
		if (savedRightLeftChild != null) {
			currentNode.leftChild.rightChild = savedRightLeftChild;
		}
		
		this.numberOfRotations += 1;

	}

	private void leftRotationHandler(BinaryNode currentNode) {
		BinaryNode savedLeftRightChild = null;
		BinaryNode heldRightChild = currentNode.rightChild;
		BinaryNode heldLeftChild = currentNode.leftChild;
		T savedElement = currentNode.element;
		
		if (currentNode.leftChild.rightChild != null && currentNode.leftChild.leftChild == null){
			
			
			currentNode.leftChild.leftChild = new BinaryNode(currentNode.leftChild.rightChild.element);
			currentNode.leftChild.leftChild.element = currentNode.leftChild.element;
			currentNode.leftChild.element = currentNode.leftChild.rightChild.element;
			
			currentNode.leftChild.rightChild = null;
			this.numberOfRotations += 1;
			
			heldRightChild = currentNode.rightChild;
			heldLeftChild = currentNode.leftChild;
			savedElement = currentNode.element;
		}
		
		
		if (currentNode.leftChild.rightChild != null) {
			savedLeftRightChild = currentNode.leftChild.rightChild;
			

		} 
		currentNode.element = currentNode.leftChild.element;
		currentNode.leftChild = currentNode.leftChild.leftChild;
		currentNode.rightChild = new BinaryNode(savedElement);
		currentNode.rightChild.rightChild = heldRightChild;
		

		 if (savedLeftRightChild != null) {
		 currentNode.rightChild.leftChild = savedLeftRightChild;
		 }
		this.numberOfRotations += 1;
	}

	private boolean BalencedHandler(BinaryNode currentNode) {

		int leftHightValue = -1;
		int rightHightValue = -1;

		if (currentNode.leftChild != null) {
			leftHightValue = currentNode.leftChild.getheight();
		}
		if (currentNode.rightChild != null) {
			rightHightValue = currentNode.rightChild.getheight();
		}

		if (leftHightValue - rightHightValue >= 2) {

			leftRotationHandler(currentNode);

		}
		if (rightHightValue - leftHightValue >= 2) {

			rightRotationHandler(currentNode);
		}

		return true;

	}

	public int height() {
		if (root == null) {
			return -1;
		}
		return root.getheight();

	}

	public boolean isEmpty() {
		if (root == null) {
			return true;
		}
		if (root.getheight() == -1) {
			return true;
		}
		return false;
	}

	public int size() {
		return this.size;
	}

	public ArrayList<Integer> toArrayList() {
		ArrayList<Integer> items = new ArrayList();
		InOrderTreeItorator tree = new InOrderTreeItorator(this.root);
		while (tree.hasNext()) {
			items.add((Integer) tree.next());
		}

		return items;
	}

	public Object[] toArray() {
		TreeItorator tree = new TreeItorator(this.root);
		Object[] items = new Object[this.size];
		for (int i = 0; i < this.size; i++) {
			items[i] = tree.next();
		}

		return items;
	}

	public String toString() {
		ArrayList<Integer> treeArray = toArrayList();

		return treeArray.toString();

	}

	@Override
	public Iterator<T> iterator() {
		return new TreeItorator(root);
	}
	public class InOrderTreeItorator<T> implements Iterator<T> {
		Object itemToRemove;
		boolean needToRemove = false;
		boolean nextRun = false;
		BinaryNode roott;
		BinaryNode itemToPop;
		Stack<BinaryNode> lazyStack = new Stack<BinaryNode>();

		public InOrderTreeItorator(BinaryNode root) {
			this.roott = root;

			while (this.roott != null) {
				if (this.needToRemove) {
					if (this.roott.getElement().equals(this.itemToRemove)) {
						this.needToRemove = false;
						this.roott = roott.getLeftChild();

					}
				}

				lazyStack.push(roott);
				this.roott = roott.getLeftChild();

			}
		}

		@Override
		public boolean hasNext() {
			return !this.lazyStack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			nextRun = true;

			itemToPop = lazyStack.pop();
			if (itemToPop.getRightChild() != null) {
				BinaryNode theRightChild = itemToPop.getRightChild();

				while (theRightChild != null) {
					lazyStack.push(theRightChild);
					theRightChild = theRightChild.getLeftChild();

				}
			}

			return (T) itemToPop.getElement();
		}

		public void remove(){
			if(!hasNext()){
				throw new IllegalStateException();
			}
			if (!nextRun){
				throw new IllegalStateException();
			}
			
			
			
			AVLTree.this.remove(itemToPop.getElement());

			this.nextRun = false;
			
		}
		

	}

	public class TreeItorator<T> implements Iterator<T> {
		Stack<BinaryNode> lazyStack = new Stack<BinaryNode>();
		BinaryNode root;
		boolean isNull;
		boolean nextRun = false;
		BinaryNode itemPop;

		public TreeItorator(BinaryNode root) {

			this.root = root;
			this.lazyStack.push(this.root);
			this.isNull = this.root == null;
		}

		@Override
		public boolean hasNext() {

			return !this.lazyStack.isEmpty() && !this.isNull;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			nextRun = true;

			itemPop = lazyStack.pop();

			if (itemPop.rightChild != null) {
				this.lazyStack.push(itemPop.rightChild);

			}
			if (itemPop.leftChild != null) {
				this.lazyStack.push(itemPop.leftChild);
			}

			return (T) (itemPop.element);

		}

		public void remove() {

			if (!nextRun) {
				throw new IllegalStateException();
			}
			AVLTree.this.remove(this.itemPop.element);
			this.lazyStack.clear();

			this.lazyStack.push(AVLTree.this.root);
			
			this.nextRun = false;

		}

	}

	public class BinaryNode {
		protected T element;
		protected BinaryNode leftChild;
		protected BinaryNode rightChild;

		public BinaryNode(T element) {
			this.element = element;
			this.leftChild = null;
			this.rightChild = null;
		}

		public void setLeftChild(BinaryNode leftChild) {
			this.leftChild = leftChild;
		}

		public void setRightChild(BinaryNode rightChild) {
			this.rightChild = rightChild;
		}

		public BinaryNode getLeftChild() {
			return leftChild;
		}

		public BinaryNode getRightChild() {
			return rightChild;
		}

		public T getElement() {
			return element;
		}

		public int getheight() {

			int lHeight = -1;
			if (leftChild != null) {
				lHeight = leftChild.getheight();
			}

			int rHeight = -1;
			if (rightChild != null) {
				rHeight = rightChild.getheight();
			}

			if (lHeight > rHeight) {
				return lHeight + 1;
			}
			return rHeight + 1;

		}

		private BinaryNode removeFinder(BinaryNode findRoot) {
			if (findRoot.rightChild != null) {
				return removeFinder(findRoot.rightChild);

			}
			return findRoot;
		}

		public BinaryNode remove(T itemToRemove, BnB wrapper) {
			BinaryNode setUp = new BinaryNode(null);
			if (itemToRemove.compareTo(this.element) == 0) {

				if (this.leftChild != null && this.rightChild != null) {
					T correctElement = removeFinder(this.leftChild).element;
					this.element = correctElement;
					this.leftChild = this.leftChild.remove(correctElement, wrapper);
					BalencedHandler(this);
					wrapper.modified = true;

					return this;
				}

				if (this.leftChild != null) {
					wrapper.modified = true;
					return this.leftChild;
				}
				if (this.rightChild != null) {
					wrapper.modified = true;
					return this.rightChild;

				}
				wrapper.modified = true;
				return null;
			}
			if (itemToRemove.compareTo(this.element) == -1) {
				if (this.leftChild == null) {
					wrapper.modified = true;
					return null;
				}
				this.leftChild = this.leftChild.remove(itemToRemove, wrapper);
				BalencedHandler(this);
				wrapper.modified = true;
				return (this);
			}
			if (itemToRemove.compareTo(this.element) == 1) {
				if (this.rightChild == null) {
					wrapper.modified = true;
					return null;
				}
				this.rightChild = this.rightChild.remove(itemToRemove, wrapper);
				wrapper.modified = true;
				BalencedHandler(this);
				
				return (this);
			}
			wrapper.modified = true;
			return null;
		}

	}

	private class BnB {
		public boolean modified;

		public BnB() {
			this.modified = false;
		}
	}

	public boolean remove(T object) {
		BnB savedState = new BnB();
		if (object == null) {
			throw new IllegalArgumentException();
		}
		if (this.root == null) {
			return false;
		}

		this.root = this.root.remove(object, savedState);
		this.size -= 1;
		if (savedState.modified) {
			
			if (this.root == null){
				this.size = 0;
			}
			return true;
		}

		return false;

	}

	public int getRotationCount() {
		return this.numberOfRotations;
	}

}
