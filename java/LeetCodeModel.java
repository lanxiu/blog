 
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public class LeetCodeModel {

	//LeetCode 3：无重复字符的最长子串

	public int lengthOfLongestSubstring(String s) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		int left = 0, max = 0;
		for (int right = 0; right < s.length(); right++) {

			char tmp = s.charAt(right);

			if (map.containsKey(tmp))
				// ERROR +1 漏了
				left = Math.max(left, map.get(tmp) + 1);

			map.put(tmp, right);

			max = Math.max(max, right - left + 1);

		}

		return max;

	}

	 

	// 至多包含 K 个不同字符的最长子串

	public int lengthOfLongestSubstring2(String s, int k) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();

		int left = 0, max = 0;

		for (int right = 0; right < s.length(); right++) {

			char tmp = s.charAt(right);

			map.put(tmp, map.getOrDefault(tmp, 0) + 1);

			// 字符数大于k时候，左指针移动，删除一个字符

			while (map.size() > k) {

				char tmp2 = s.charAt(left);
				map.put(tmp2, map.get(tmp2) - 1);
				if (map.get(tmp2) == 0)
					map.remove(tmp2);

				left++;

			}
			// error
			// max = Math.max(max, map.size());

			max = Math.max(max, right - left + 1);
		}

		return max;

	}

	// 76 最小覆盖子串
	// 给定两个字符串 s 和 t，长度分别是 m 和 n，返回 s 中的 最短窗口 子串，
	// 使得该子串包含 t 中的每一个字符（包括重复字符）。如果没有这样的子串，返回空字符串 ""。

	public int lengthOfLongestSubstring3(String s, String t) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		int required = t.length();

		for (int i = 0; i < t.length(); i++) {
			map.put(t.charAt(i), map.getOrDefault(t.charAt(i), 0) + 1);
		}

		int left = 0, min = t.length();

		for (int right = 0; right < s.length(); right++) {

			char tmp = s.charAt(right);

			if (map.containsKey(tmp)) {

				map.put(tmp, map.get(tmp) - 1);

				required--;

			}

			while (map.get(s.charAt(left)) < 0
					|| map.containsKey(s.charAt(left)) == false) {
				map.putIfAbsent(s.charAt(left), map.get(s.charAt(left)) + 1);
				left++;
			}

			if (required < 1)
				min = Math.min(min, right - left + 1);

		}

		return min;

	}

	// 76. 最小覆盖子串
	public String lengthOfLongestSubstring3Modify(String s, String t) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		int required = t.length();

		for (int i = 0; i < t.length(); i++) {
			map.put(t.charAt(i), map.getOrDefault(t.charAt(i), 0) + 1);
		}

		// 边界错误
		int left = 0, min = s.length() + 1;
		String result = "";

		for (int right = 0; right < s.length(); right++) {

			char tmp = s.charAt(right);
			// 右指针判断是否完全包含了t

			if (map.containsKey(tmp)) {
				map.put(tmp, map.get(tmp) - 1);
				if (map.get(tmp) >= 0)
					required--;
			}

			if (required > 0)
				continue;

			// 循环过多，有错误，需要优化
			// 左指针优化，去掉没有的，去掉可能会多的
			while (true) {

				if (map.containsKey(s.charAt(left)) == false) {
					left++;
					continue;
				}

				if (map.get(s.charAt(left)) < 0) {

					map.put(s.charAt(left), map.get(s.charAt(left)) + 1);
					left++;
					continue;
				}
				break;
			}

			if (min > right - left + 1) {
				min = Math.min(min, right - left + 1);
				result = s.substring(left, right + 1);

			}

		}

		return result;

	}

	public String lengthOfLongestSubstring4Modify(String s, String t) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		int required = t.length();

		for (int i = 0; i < t.length(); i++) {
			map.put(t.charAt(i), map.getOrDefault(t.charAt(i), 0) + 1);
		}

		// 边界错误
		int left = 0, min = s.length() + 1;
		String result = "";

		for (int right = 0; right < s.length(); right++) {

			char tmp = s.charAt(right);
			// 右指针判断是否完全包含了t

			if (map.containsKey(tmp)) {

				map.put(tmp, map.get(tmp) - 1);
				if (map.get(tmp) >= 0)
					required--;
			}

			if (required > 0)
				continue;

			// ai 优化

			while (required == 0) {

				if (min > right - left + 1) {
					min = right - left + 1;
					result = s.substring(left, right + 1);
				}

				char lc = s.charAt(left);
				if (map.containsKey(lc)) {
					map.put(lc, map.get(lc) + 1);
					if (map.get(lc) > 0) {
						required++;
					}
				}
				left++;
			}

		}

		return result;

	}

	// 560 和为k的子数组

	public int subarraySum(int[] nums, int k) {

		// value count
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		//没有这行代码，会漏掉所有 “从开头开始且和为 k” 的子数组（比如[3]、[1,2]这类场景）。
		map.put(0, 1);
		// value index
		int sum = 0;
		int result = 0;
		for (int a : nums) {
			sum += a;
			map.put(sum, map.getOrDefault(sum, 0) + 1);

			if (k != 0 && map.containsKey(sum - k)) {
				result += map.get(sum - k);
			}

			// k = 0 为啥要作特殊处理  ,因为前边有个 0,1的键值对存了进去
			if (k == 0 && map.containsKey(sum - k)) {
				result += map.get(sum - k) - 1;
			}

		}

		// Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
		// while (it.hasNext()) {
		// sum = it.next().getKey();
		// if ( map.containsKey(sum - k)) {
		// result += map.get(sum - k) * map.get(sum);
		// }
		// }

		return result;

	}

	// 1 两数之和

	public int[] twoSum(int[] nums, int target) {

		int[] result = new int[2];
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++) {
			int tmp = nums[i];
			if (map.containsKey(target - tmp)) {
				result[0] = map.get(target - tmp);
				result[1] = i;
				return result;

			}
			map.put(tmp, i);

		}

		return null;
	}

	// 438. 找到字符串中所有字母异位词

	// 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
	public List<Integer> findAnagrams(String s, String p) {

		// Map<Character, Integer> map1 = new HashMap<Character, Integer>();
		Map<Character, Integer> map2 = new HashMap<Character, Integer>();
		int required1 = 0;
		int required2 = 0;
		for (char a : p.toCharArray()) {
			// map1.put(a, map1.getOrDefault(a, 0) + 1);
			map2.put(a, map2.getOrDefault(a, 0) + 1);
			required2++;
		}

		List<Integer> lst = new ArrayList<Integer>();
		int left = 0;
		int right = 0;

		while (right < s.length()) {
			char tmp = s.charAt(right);
			right++;
			if (map2.containsKey(tmp)) {
				map2.put(tmp, map2.get(tmp) - 1);

				if (map2.get(tmp) >= 0)
					required2--;
				else
					required1++;

			}

			if (right - left != p.length())
				continue;

			if (required2 == 0 && required1 == 0)

				lst.add(left);

			char tmp2 = s.charAt(left);
			left++;

			if (map2.containsKey(tmp2)) {

				map2.put(tmp2, map2.get(tmp2) + 1);
				if (map2.get(tmp2) > 0)
					required2++;
				else
					required1--;
			}

		}

		return lst;
	}

	// 704. 二分查找
	public int search(int[] nums, int target) {

		int left = 0;
		int right = nums.length - 1;

		int mid = (left + right) / 2;
		while (left <= right) {

			if (nums[mid] > target) {
				right = mid - 1;
				mid = (left + right) / 2;
			}

			else if (nums[mid] < target) {
				left = mid + 1;
				mid = (left + right) / 2;

			} else {
				return mid;
			}

		}

		return -1;

	}

	// TODO: 需要加深理解
	// 34. 在排序数组中查找元素的第一个和最后一个位置

	public int[] searchRange(int[] nums, int target) {

		int left = 0;
		int right = nums.length - 1;
		int mid = -1;
		int[] result = new int[2];
		boolean boo = false;

		while (left <= right) {
			mid = (left + right) / 2;
			if (nums[mid] > target) {
				right = mid - 1;

			}

			else if (nums[mid] < target) {
				left = mid + 1;

			} else {
				boo = true;
				break;
			}

		}
		if (boo == false) {
			result[0] = -1;
			result[1] = -1;
			return result;
		}
		int min = mid;
		while (min > -1 && nums[min] == target) {
			result[0] = min;
			min--;

		}

		while (mid < nums.length && nums[mid] == target) {
			result[1] = mid;
			mid++;

		}

		return result;

	}

	public int[] searchRange2(int[] nums, int target) {

		int left = 0;
		int right = nums.length - 1;
		int mid = -1;
		int[] result = new int[2];
		boolean boo = false;

		while (left <= right) {
			mid = (left + right) / 2;
			if (nums[mid] > target) {
				right = mid - 1;

			}

			else if (nums[mid] < target) {
				left = mid + 1;

			} else {
				boo = true;
				break;
			}

		}
		if (boo == false) {
			result[0] = -1;
			result[1] = -1;
			return result;
		}
		int index = mid;
		left = mid;
		right = nums.length;
		while (left < right) {
			mid = (left + right) / 2;
			if (nums[mid] < target) {
				left = mid + 1;
			} else if (nums[mid] > target) {
				right = mid;
			} else
				left = mid + 1;
		}
		result[1] = left - 1;

		left = 0;
		right = index;
		while (left < right) {
			mid = (left + right) / 2;
			if (nums[mid] < target)
				left = mid + 1;
			else if (nums[mid] > target)
				right = mid;
			else
				left = mid;

		}
		result[0] = left;
		return result;

	}

	public int[] searchRangeModel(int[] nums, int target) {
		int[] res = new int[2];
		res[0] = lowerBound(nums, target);
		res[1] = upperBound(nums, target) - 1; // upperBound 返回的是第一个 > target
		if (res[0] <= res[1])
			return res;
		return new int[]{-1, -1};
	}

	// 找第一个 >= target
	private int lowerBound(int[] nums, int target) {
		int left = 0, right = nums.length; // 注意 right = nums.length
		while (left < right) {
			int mid = left + (right - left) / 2;
			if (nums[mid] < target)
				left = mid + 1;
			else
				right = mid;
		}
		return left;
	}

	// 找第一个 > target
	private int upperBound(int[] nums, int target) {
		int left = 0, right = nums.length;
		while (left < right) {
			int mid = left + (right - left) / 2;
			if (nums[mid] <= target)
				left = mid + 1;
			else
				right = mid;
		}
		return left;
	}

	// 153. 寻找旋转排序数组中的最小值
	public int findMin(int[] nums) {

		int left = 0, right = nums.length - 1, mid = 0;
		while (left < right) {

			mid = (left + right) / 2;

			if (nums[mid] > nums[right]) {
				left = mid + 1;
			} else {
				right = mid;
			}

		}

		return nums[left];
	}

	// 33 搜索旋转排序数组
	public int search22(int[] nums, int target) {

		int left = 0, right = nums.length - 1, mid = 0;

		while (left < right) {
			mid = (left + right) / 2;

			if (nums[mid] > nums[right]) {
				left = mid + 1;
			} else {
				right = mid;
			}

		}

		if (target > nums[nums.length - 1]) {
			right = left - 1;
			left = 0;

		} else {
			right = nums.length - 1;
		}

		while (left <= right) {
			mid = (left + right) / 2;

			if (nums[mid] > target) {
				right = mid - 1;
			} else if (nums[mid] < target) {
				left = mid + 1;
			} else {
				return mid;
			}

		}

		return -1;

	}

	public int search111(int[] nums, int target) {

		int left = 0, right = nums.length - 1, mid = 0;

		while (left <= right) {

			mid = (left + right) / 2;

			if (mid > target)
				right = mid - 1;
			else if (mid < target)
				left = mid + 1;
			else
				return mid;

		}

		return -1;
	}

	// 739. 每日温度

	public int[] dailyTemperatures(int[] temperatures) {

		Stack<Integer> vector = new Stack<Integer>();
		// Stack<Integer> vector2 = new Stack<Integer>();
		int[] result = new int[temperatures.length];
		for (int i = 0; i < temperatures.length; i++) {

			int tmp = temperatures[i];
			while (vector.isEmpty() == false
					&& temperatures[vector.peek()] < tmp) {

				// vector.pop();
				// 没有保存原有索引
				result[vector.peek()] = i - vector.pop();

			}
			vector.push(i);
			// vector2.push(i);

		}

		result[temperatures.length - 1] = 0;

		return result;

	}
	public int[] dailyTemperatures22(int[] temperatures) {
		int n = temperatures.length;
		Stack<Integer> vector = new Stack<Integer>();
		// Stack<Integer> vector2 = new Stack<Integer>();
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {

			while (!vector.isEmpty()
					&& temperatures[vector.peek()] < temperatures[i]) {

				// vector.pop();
				// 没有保存原有索引
				result[vector.peek()] = i - vector.pop();

			}
			vector.push(i);
			// vector2.push(i);

		}

		return result;

	}

	// 84. 柱状图中最大的矩形
	public int largestRectangleArea11(int[] heights) {

		int areaHeight = heights[0];
		int areaLenth = 1;
		int tmpArea = areaHeight * areaLenth;
		int max = 0;
		for (int i = 0; i < heights.length; i++) {

			int tmp = heights[i];
			int left = i;
			int right = i;
			while (left >= 0 && heights[left] >= tmp)
				left--;
			while (right < heights.length && heights[right] >= tmp)
				right++;

			int lenthTmp = right - left - 2;

			tmpArea = lenthTmp * tmp;
			if (tmpArea > max)
				max = tmpArea;

		}

		return max;
	}

	// 84. 柱状图中最大的矩形 2
	public int largestRectangleArea22(int[] heights) {

		int max = 0;

		// 高度
		Stack<Integer> stack = new Stack<Integer>();
		// 长度
		Stack<Integer> stack2 = new Stack<Integer>();
		// 索引
		Stack<Integer> stack3 = new Stack<Integer>();

		int count = 0;
		for (int i = 0; i < heights.length; i++) {
			count = 0;
			int tmp = heights[i];

			while (stack.isEmpty() == false) {
				if (tmp > stack.peek()) {
					stack.push(tmp);
					stack2.push(count + 1);
					stack3.push(i);
					break;
				} else if (tmp < stack.peek()) {

					int a = stack.pop();
					int b = stack2.pop();
					int c = stack3.pop();

					count += b;
					if (a * count > max)
						max = a * count;

					int area = (i - c + 1) * heights[i];

					if (area > max)
						max = area;

				} else {
					stack2.push(stack2.pop() + count + 1);
					stack3.push(i);
					break;
				}

			}
			if (stack.isEmpty() && tmp != 0) {
				stack.push(tmp);
				stack2.push(count + 1);
			}
		}
		int y = 0;
		while (stack.isEmpty() == false) {

			int x = stack.pop();
			y += stack2.pop();
			if (x * y > max)
				max = x * y;

		}

		return max;
	}
	public int largestRectangleArea44(int[] heights) {

		int max = 0;
		int tmp = 0;
		// 索引
		Stack<Integer> stack3 = new Stack<Integer>();

		for (int i = 0; i <= heights.length; i++) {

			if (i == heights.length)
				tmp = 0;
			else
				tmp = heights[i];

			while (true) {

				if (stack3.isEmpty() || tmp >= heights[stack3.peek()]) {

					stack3.push(i);
					break;
				} else if (tmp < heights[stack3.peek()]) {

					int c = stack3.pop();
					int d = -1;
					if (stack3.isEmpty() == false)
						d = stack3.peek();

					int area = (i - d - 1) * heights[c];

					if (area > max)
						max = area;

				}

			}
		}

		return max;
	}

	// 42. 接雨水

	public int trap(int[] height) {

		int length = height.length;

		Stack<Integer> stack = new Stack<Integer>();

		int tmp = 0;
		int area = 0;
		for (int i = 0; i < length; i++) {
			tmp = height[i];
			while (true) {

				if (stack.isEmpty() == false && tmp > height[stack.peek()]) {

					int peek = stack.pop();
					int left = 0;
					if (stack.isEmpty() == false)
						left = stack.peek();
					else {

						stack.push(i);

						break;
					}
					if (tmp > height[left]) {
						area += (height[left] - height[peek]) * (i - left - 1);
					} else {

						area += (tmp - height[peek]) * (i - left - 1);
					}

				} else {

					stack.push(i);
					break;
				}

			}
		}

		return area;

	}// 42. 接雨水

	public int trap22(int[] height) {

		int length = height.length;

		Stack<Integer> stack = new Stack<Integer>();

		int tmp = 0;
		int area = 0;
		for (int i = 0; i < length; i++) {
			tmp = height[i];
			while (stack.isEmpty() == false && tmp > height[stack.peek()]) {

				int peek = stack.pop();

				if (stack.isEmpty())
					break;
				int left = stack.peek();

				area += (Math.min(height[left], tmp) - height[peek])
						* (i - left - 1);

			}
			stack.push(i);
		}

		return area;

	}

	// 46. 全排列

	// 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
	public List<List<Integer>> permute(int[] nums) {

		List<Integer> tmp;
		int n = nums.length;
		int[] path = new int[n];
		boolean[] used = new boolean[n];
		List<List<Integer>> result = new ArrayList<List<Integer>>();

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		int count = 0;
		tmp = new ArrayList<Integer>();
		int j = 0;
		int i = 0;
		while (count > -1) {
			for (i = 0; i < n; i++) {

				if (used[i] == false) {

					tmp.add(nums[i]);
					used[i] = true;
					count++;

					if (count == n) {
						List<Integer> resTmp = new ArrayList<Integer>();
						resTmp.addAll(tmp);
						result.add(resTmp);

					}
				}

			}

			j++;

		}

		return null;

	}

	public List<List<Integer>> permute2(int[] nums) {
		int n = nums.length;
		boolean[] used = new boolean[n];
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		List<Integer> path = new ArrayList<Integer>();
		dfs(path, nums, used, res);

		return res;
	}

	void dfs(List<Integer> path, int[] nums, boolean[] used,
			List<List<Integer>> res) {

		System.out.println(" 》》》进入方法dfs  ");
		System.out.println(path);
		System.out.println(used[0] + "  " + used[1]);
		if (path.size() == nums.length) {
			res.add(new ArrayList<>(path));

			System.out.println(" 《《《进入方法返回dfs  ");
			System.out.println(path);
			System.out.println(used[0] + "  " + used[1]);

			return;
		}

		for (int i = 0; i < nums.length; i++) {
			if (used[i])
				continue;

			used[i] = true;
			path.add(nums[i]);
			System.out.println(" i 此时为：" + i);

			dfs(path, nums, used, res);

			path.remove(path.size() - 1);
			used[i] = false;
		}

		System.out.println(" 《《《进入方法循环dfs  ");
		System.out.println(path);
		System.out.println(used[0] + "  " + used[1]);
	}

	// 78. 子集
	public List<List<Integer>> subsets(int[] nums) {

		List<Integer> path = new ArrayList<Integer>();

		List<List<Integer>> result = new ArrayList<List<Integer>>();
		// result.add(new ArrayList<Integer>());
		boolean[] used = new boolean[nums.length];
		// dfsSubset(path, nums, used, result, 0);;
		dfsSubset(path, nums, result, 0);;

		return result;
	}

	public void dfsSubset(List<Integer> path, int[] nums, boolean[] used,
			List<List<Integer>> result, int j) {

		for (; j < nums.length; j++) {

			if (used[j])
				continue;

			used[j] = true;
			path.add(nums[j]);

			dfsSubset(path, nums, used, result, j + 1);

			result.add(new ArrayList<Integer>(path));
			path.remove(path.size() - 1);
			used[j] = false;

		}

	}
	public void dfsSubset2(List<Integer> path, int[] nums,
			List<List<Integer>> result, int j) {

		for (; j < nums.length; j++) {

			path.add(nums[j]);

			dfsSubset(path, nums, result, j + 1);
			// 未处理空子集
			result.add(new ArrayList<Integer>(path));
			path.remove(path.size() - 1);

		}

	}
	public void dfsSubset(List<Integer> path, int[] nums,
			List<List<Integer>> result, int j) {

		result.add(new ArrayList<Integer>(path));
		for (; j < nums.length; j++) {

			path.add(nums[j]);

			dfsSubset(path, nums, result, j + 1);

			path.remove(path.size() - 1);

		}

	}
	// 39. 组合总和
	public List<List<Integer>> combinationSum(int[] candidates, int target) {

		List<Integer> path = new ArrayList<Integer>();

		List<List<Integer>> result = new ArrayList<List<Integer>>();

		dfscombinationSum(path, candidates, result, 0, target);;

		return result;

	}

	public void dfscombinationSum2(List<Integer> path, int[] nums,
			List<List<Integer>> result, int j, int target, int count) {

		// 可以用count-target 代替
		if (count > target)
			return;
		else if (count == target) {
			result.add(new ArrayList<Integer>(path));
			return;
		}

		for (; j < nums.length; j++) {

			path.add(nums[j]);
			count += nums[j];

			dfscombinationSum2(path, nums, result, j, target, count);
			count -= nums[j];

			path.remove(path.size() - 1);

		}

	}

	public void dfscombinationSum(List<Integer> path, int[] nums,
			List<List<Integer>> result, int j, int remain) {

		if (remain < 0)
			return;
		if (remain == 0) {
			result.add(new ArrayList<Integer>(path));
			return;
		}

		for (; j < nums.length; j++) {

			path.add(nums[j]);

			dfscombinationSum(path, nums, result, j, remain - nums[j]);

			path.remove(path.size() - 1);

		}

	}

	// 102. 二叉树的层序遍历

	/**
	 * Definition for a binary tree node.
	 */
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode() {
		}
		TreeNode(int val) {
			this.val = val;
		}
		TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	public List<List<Integer>> levelOrder(TreeNode root) {
		
		if(root == null)
			return new ArrayList<List<Integer>>();

		Queue<TreeNode> q = new ArrayDeque<LeetCodeModel.TreeNode>();

		q.offer(root);
		List<Integer> tmp = new ArrayList<Integer>();
		List<List<Integer>> reslut = new ArrayList<List<Integer>>();
		while (q.isEmpty() == false) {
			int size = q.size();

			// 注意更更新
			tmp = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {

				TreeNode tn = q.poll();
				tmp.add(tn.val);

				if (tn.left != null)
					q.offer(tn.left);
				if (tn.right != null)
					q.offer(tn.right);

			}
			reslut.add(tmp);

		}

		return reslut;
	}
	
	//  107. 二叉树的层序遍历 II
	public List<List<Integer>> levelOrderBottom(TreeNode root) {

		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (root == null)
			return result;
		Queue<TreeNode> q = new ArrayDeque<TreeNode>();
		List<Integer> tmp;
		q.offer(root);
		while (q.isEmpty() == false) {
			int size = q.size();
			tmp = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {
				TreeNode tn = q.poll();
				tmp.add(tn.val);
				if (tn.left != null)
					q.offer(tn.left);
				if (tn.right != null)
					q.offer(tn.right);

			}
			result.add(0, tmp);

		}

		return result;

	}
	
	//103. 二叉树的锯齿形层序遍历
	public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (root == null)
			return result;
		Queue<TreeNode> q = new ArrayDeque<TreeNode>();
		List<Integer> tmp;
		q.offer(root);
		boolean boo = true;
		while (q.isEmpty() == false) {
			int size = q.size();
			tmp = new ArrayList<Integer>();

			for (int i = 0; i < size; i++) {
				TreeNode tn = q.poll();
				if (boo) {

					tmp.add(tn.val);

				} else {

					tmp.add(0, tn.val);

				}
				if (tn.left != null)
					q.offer(tn.left);
				if (tn.right != null)
					q.offer(tn.right);

			}
			if (boo)
				boo = false;
			else
				boo = true;
			result.add(tmp);

		}

		return result;

	}
	
	public List<List<Integer>> permuteaa(int[] nums) {

		List<Integer> path = new ArrayList<Integer>();
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		boolean[] used = new boolean[nums.length];
		dfspermuteaa(nums, path, result, used);

		return result;

	}

	public void dfspermuteaa(int nums[], List<Integer> path,
			List<List<Integer>> result, boolean[] used) {

		if (path.size() == nums.length) {
			 
			// result.add(path);
			result.add(new ArrayList<Integer>(path));
			return;
		}

		for (int i = 0; i <  nums.length; i++) {
			if (used[i])
				continue;
			path.add(nums[i]);
			used[i] = true;

			dfspermuteaa(nums, path, result, used);

			path.remove(path.size() - 1);
			used[i] = false;

		}

	}
    
	//  104. 二叉树的最大深度
    public int maxDepth(TreeNode root) {
    	
    	if(root == null)
    		return 0;
    	
    	//  可以优化掉
//    	if(root.left == null && root.right == null)
//    		return 1;
     
    	
    	int depth = 0;
    	int left = 0, right = 0;
    	//可以优化掉
    	if(root.left != null)
    		  left = maxDepth(root.left);
    		  
    	//	  可以优化掉
    	if(root.right!= null)
    		right = maxDepth(root.right);
    	
    	depth  = Math.max(left, right)+1;
    	
    	
    	return depth;
    }
    
    

	//  543. 二叉树的直径
    public int diameterOfBinaryTree(TreeNode root) {
    	// 为啥是传值 
    	//Integer width = new Integer(0);
    	
    	List<Integer> width = new ArrayList<Integer>();
    	width.add(0);
    	this.maxDepth(root,width);
    	return width.get(0);
        
    }
	public int maxDepth(TreeNode root, List<Integer> width) {

		if (root == null)
			return 0;

		int depth = 0;
		int left = 0, right = 0;

		left = maxDepth(root.left, width);

		right = maxDepth(root.right, width);

		depth = Math.max(left, right) + 1;

		if (left + right > width.get(0))
			width.set(0, left + right);

		return depth;
	}
    
    // 236. 二叉树的最近公共祖先
	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p,
			TreeNode q) {
		List<TreeNode> parent = new ArrayList< TreeNode>(); 
		dfs(root, p, q, parent);
		return parent.get(0);
	}

	public int dfs(TreeNode root, TreeNode p, TreeNode q,   List<TreeNode> parent) {

		if (parent.size() == 1)
			return 2;
		if(root == null)
			return 0;

		int result = 0;
		if (root == p || root == q)
			result = 1;

		int left, right;

		left = dfs(root.left, p, q, parent);
		right = dfs(root.right, p, q, parent);

		if (left + right + result == 2) {
 
			parent.add(root);
			
//			return 2;
		}

		return left + right +result;

	}
    
    public int aatest() {
    	Integer aa =0;aa += 1;
    	aa+=aa(aa); 
    	System.out.println("aa change ? :" + aa);
    	return aa;
    }
    public int aa(Integer aa) {
    	  aa =0;aa += 1;
    	aa ++;
    	int a = new Random().nextInt(20);
    	if(a == 6)
    		return aa;
    	aa(aa);
    	System.out.println("aa inner: " + aa);
    	return aa;
    }
    
	// 70. 爬楼梯

	int[] aa = new int[45];

	public int climbStairs2(int n) {

		int sum = 0;
		// 跳 1 2 0 步数
		if (n >= 2) {
			// 防止重复
			if (aa[n - 2] == 0) {
				aa[n - 2] = climbStairs(n - 2);
			}
			sum += aa[n - 2];
		}

		if (n >= 1) {
			if (aa[n - 1] == 0) {
				aa[n - 1] = climbStairs(n - 1);
			}
			sum += aa[n - 1];
		}
		if (n == 0) {
			sum += 1;
		}
		return sum;
	}
	
	//70. 爬楼梯
	public int climbStairs(int n) {
 
		if( n<=1) return 1;
		//xxx
		if(aa[n]!= 0) return aa[n];
		aa[n] = climbStairs(n-1)+ climbStairs(n-2);
		return aa[n];
	}
	
	//198. 打家劫舍
	public int rob(int[] nums) {

		int[] aa = new int[nums.length];
		for (int i =0;i<nums.length;i++ )
			aa[i] = -1;

		return rob(nums, nums.length - 1, aa);
	}
    
	public int rob(int[] nums, int n,int[] aa ) {
		
		if(n < 0)
			return 0;

		if (aa[n] != -1)
			return aa[n];

		aa[n] = Math.max(rob(nums, n - 1,aa), rob(nums, n - 2,aa) + nums[n]);

		return aa[n];
	}
	
	
	//300. 最长递增子序列
	public int lengthOfLIS22(int[] nums) {
		int count = 0;
		int[] key = new int[nums.length];
		int[] value = new int[nums.length];
		int index = 0;
		for (int i = 0; i < nums.length; i++) {
			int tmp = nums[i];
			boolean boo = false;

			for (int j = 0; j <= index; j++) {
				if (tmp > key[j]) {
					boo = true;
					key[j] = tmp;
					value[j]++;
					if (value[j] > count)
						count = value[j];

				} else if(tmp == key[j]) {
					boo = true;
					if (value[j] ==0)
						value[j]++;
				}

			}
			if (boo == false) {
				index++;
				key[index] = tmp;
				value[index] = 1;

			}

		}

		return count;

	}
 
	//300. 最长递增子序列
	public int lengthOfLIS(int[] nums) {

		int[] aa = new int[nums.length + 1];
		boolean[] ab = new boolean[nums.length];
		for (int i = 0; i < nums.length-aa[nums.length]; i++)
			lengthOfLIS(nums, i, aa, ab);
		return aa[nums.length] + 1;
	}
	
	int sscount = 0;

	public int lengthOfLIS(int[] nums, int j, int[] aa, boolean[] ab) {

		if (ab[j]) {
			return aa[j];
		}

		for (int i = j + 1; i < nums.length-aa[j]  ; i++) {
//			sscount++;
//			System.out.println( "int j :" +j);
//			System.out.println( "int i :" +i);
			if (nums[i] > nums[j]) {
				int tmp = lengthOfLIS(nums, i, aa, ab) + 1;
				if (tmp > aa[j])
					aa[j] = tmp;
				if (tmp > aa[nums.length])
					aa[nums.length] = tmp;
			} 
			
//			else {
//				lengthOfLIS(nums, i, aa, ab);
//			}
		}
		ab[j] = true;
		return aa[j];

	}
	
	 public int lengthOfLISgg(int[] nums) {
	        int[] tails = new int[nums.length];
	        int res = 0;
	        for(int num : nums) {
	            int i = 0, j = res;
	            while(i < j) {
	                int m = (i + j) / 2;
	                if(tails[m] < num) i = m + 1;
	                else j = m;
	            }
	            tails[i] = num;
	            if(res == j) res++;
	            for(int tmp:tails)
	            System.out.print(tmp);
	            System.out.println();
	        }
	        return res;
	    }

 
	
	
	
	
	public static void main(String[] args) {
		LeetCodeModel test = new LeetCodeModel();
//		System.out.println(test.findAnagrams("cbaebabacd", "abc"));
//		System.out.println(20 / 3);
//		System.out.println(1 / 3);
//
//		int[] test1 = {5, 7, 7, 8, 8, 10};
//		int[] test2 = {2, 2};
		// int[] res = test.searchRange2(test1,8);
		// System.out.println(res[0]); System.out.println(res[1]);
		//
		// res = test.searchRange2(test2,2);
		// System.out.println(res[0]); System.out.println(res[1]);

//		int[] test3 = {2, 1, 5, 6, 2, 3};
//		int[] test4 = {2, 1, 2};
//		int[] test5 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
//		int tt = test.largestRectangleArea44(test4);
//		tt = test.trap(test5);
//		System.out.println("jieguo:" + tt);
//		boolean[] boo = new boolean[2];
//		System.out.println(boo[0]);
//		System.out.println(boo[1]);
		int nums[] = {0, 1};
//		test.permute2(nums);
//
//		System.err.println(false || true &&true);
//		System.err.println(false || true &&false);
//		System.err.println(false || false &&false);
//		test.aatest();
//
//		TreeNode tn1 = test.new TreeNode(1);
//		TreeNode tn0= test.new TreeNode(0);
//		TreeNode tn2 = new LeetCodeModel().new TreeNode(2);
//		tn0.left = tn1;
//		tn0.right = tn2;
//		
//		System.err.println("jieguo:" + test.lowestCommonAncestor(tn0,tn1,tn2));
		int[] aa = new int[nums.length];
		for (int t : aa)
			t = -1;
		System.err.println("jieguo11:" + aa[0]);
		int nums2[] = {7,7,7,7,7,7,7};
		int nums3[] = {7,6,5,4,3,2,1};
//		int nums4[] = {1,2,3,4,5,6,7};
		int nums4[] = {4,1,3,2,5,0,7};
		 int r11 =test.rob(nums2);
			System.out.println("rob :" + r11);

			System.out.println("lengthOfLIS 平序 :" + test.lengthOfLIS(nums2));
			System.out.println("lengthOfLIS 平序 :" + test.lengthOfLISgg(nums2));
			System.out.println("lengthOfLIS :" +test.sscount);
			test.sscount = 0;
			System.out.println("lengthOfLIS 逆序 :" + test.lengthOfLIS(nums3));
			System.out.println("lengthOfLIS 逆序 :" + test.lengthOfLISgg(nums3));
			System.out.println("lengthOfLIS :" +test.sscount);
			test.sscount = 0;
			System.out.println("lengthOfLIS 正序:" + test.lengthOfLIS(nums4));
			System.out.println("lengthOfLIS 正序:" + test.lengthOfLISgg(nums4));
			System.out.println("lengthOfLIS :" +test.sscount);

	}
}
