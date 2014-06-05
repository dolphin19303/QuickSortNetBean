package quicksort;

public class QuickSort implements Runnable {
	// mảng phần tử
	private int[] numbers;
	private int number;

	// biến cờ lưu trạng thái có thể tiếp tục chạy
	private boolean canContinue = true;
	// hàm thông báo lahị
	QuickSortCallback mCallback;

	// bắt đầu giải thuật
	public void sort(int[] values) {
		// check for empty or null array
		if (values == null || values.length == 0) {
			return;
		}
		this.numbers = values;
		number = values.length;
		(new Thread(this)).start();
	}

	// thêm đối tượng lắng nghe cho quicksort
	public void setListener(QuickSortCallback mCallback) {
		this.mCallback = mCallback;
	}

	// đệ quy giải thuật
	private void quicksort(int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		int pivot = numbers[low + (high - low) / 2];
		while (!canContinue) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Fuck, I can't sleep!");
			}
		}
		canContinue = false;
		// Divide into two lists
		while (i <= j) {

			// If the current value from the left list is smaller then the
			// pivot
			// element then get the next element from the left list
			while (numbers[i] < pivot) {
				i++;
			}
			// If the current value from the right list is larger then the
			// pivot
			// element then get the next element from the right list
			while (numbers[j] > pivot) {
				j--;
			}
			if (mCallback != null)
				mCallback.onShowFence(low, high);
			// If we have found a values in the left list which is larger
			// then
			// the pivot element and if we have found a value in the right
			// list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j) {
			quicksort(low, j);
		}
		if (i < high) {
			quicksort(i, high);
		}
	}

	// thông báo QuickSort có thể chạy bước tiếp theo
	public void notifyToContinue() {
		canContinue = true;
	}

	// thông báo lại là đang hoán đổi vị trí 2 số
	private void exchange(int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
		if (mCallback != null) {
			mCallback.onSwap(i, j);
		}

	}

	public interface QuickSortCallback {
		public void onSwap(int posA, int posB);

		public void onShowFence(int posA, int posB);
	}

	@Override
	public void run() {
		quicksort(0, number - 1);
	}
}
