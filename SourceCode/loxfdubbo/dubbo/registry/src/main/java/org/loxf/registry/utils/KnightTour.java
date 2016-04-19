/**
 * KnightTour.java
 * luohj - 上午9:30:45
 * 
 */
package org.loxf.registry.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 开跳马棋
 * 
 * @author luohj
 *
 */
public class KnightTour {
	private Position start;
	private Position end;
	private int width;
	private int height;
	private Entity[][] entityArray;
	private LinkedList<Position> result;
	private int total;
	private int startValue;
	/**
	 * 马移动向量
	 */
	private int[][] stepV = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
	/**
	 * 运算次数计算
	 */
	private BigInteger time = BigInteger.ZERO;

	public static void main(String args[]) {
		KnightTour t = new KnightTour(new Position(0, 4), new Position(1, 2));// 设置起始位置
		t.setTotal(261);// 设置行列和（200-500）
		t.setStartValue(19); // 设置起始位置值（建议两位数）
		t.setWidth(8);// 棋盘规格，8*8（建议正方形，可以长方形）
		t.setHeight(8);
		t.run();
	}

	/**
	 * @param start
	 * @param end
	 */
	public KnightTour(Position start, Position end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * TODO:运行骑士漫游
	 * 
	 * @author:luohj
	 */
	public void run() {
		if (preCheck()) {
			Date start_d = new Date();
			init();
			Date end_d = new Date();
			System.out.println("初始化运行时间：" + (end_d.getTime() - start_d.getTime()) + "毫秒");
			start_d = new Date();
			result = getResult(result);
			end_d = new Date();
			System.out.println("求解运行时间：" + (end_d.getTime() - start_d.getTime()) + "毫秒");
			if (result == null) {
				System.out.println("当前起始设定无解。共运行" + time + "次！");
			} else {
				System.out.println("当前起始设定有解。共运行" + time + "次！");
				for (int i = 0; i < result.size(); i++) {
					Position pos = result.get(i);
					entityArray[pos.row][pos.col].step = i + 1;
				}
				printResult();
			}
		}
	}

	/**
	 * TODO:打印结果
	 * 
	 * @author:luohj
	 */
	public void printResult() {
		for (Entity[] rows : entityArray) {
			for (Entity entity : rows) {
				if (entity.pos.equals(start))
					entity.printBold();
				else
					entity.print();
			}
			System.out.println("");
		}
	}

	/**
	 * 预校验
	 * 
	 * @return
	 * @author:luohj
	 */
	boolean preCheck() {
		if (start == null || end == null) {
			System.out.println("请设定开始位置和结束位置！");
			return false;
		}
		if (start.equals(end)) {
			System.out.println("开始位置和结束位置不能相同！");
			return false;
		}
		if (total < 200 && total > 500) {
			System.out.println("运算横竖和值必须在200~500之间。");
			return false;
		}
		if (!(between(start.row, 0, height) && between(start.col, 0, width) && between(end.row, 0, height)
				&& between(end.col, 0, width))) {
			System.out.println("位置的行和列应该为在棋盘的行数和列数之内的整数！");
			return false;
		}
		return true;
	}

	/**
	 * 初始化棋盘
	 * 
	 * @author:luohj
	 */
	void init() {
		// 变量初始化
		entityArray = new Entity[height][width];
		result = new LinkedList<Position>();
		// 计算棋盘未指定值的格子的初始值，并生成标准值数组
		int[] standValue = new int[width];
		int sum = 0;
		for (int i = 0; i < width - 2; i++) {
			standValue[i] = i + 1;
			sum += i + 1;
		}
		int anotherNbr = total - startValue - sum;
		standValue[width - 2] = anotherNbr;
		standValue[width - 1] = startValue;

		int tmpValue = standValue[start.col];
		standValue[start.col] = startValue;
		standValue[standValue.length - 1] = tmpValue;

		// 初始化棋盘
		for (int row = 0; row < height; row++) {// 行号
			for (int col = 0; col < width; col++) {// 列号
				// 初始化当前格子
				Entity curr = new Entity(row, col);
				// 计算获取当前格子的下一可能的所有格子
				List<Position> allNext = new ArrayList<Position>();
				for(int [] s : stepV){
					if (between(row + s[0], 0, height) && between(col + s[1], 0, width)) {
						allNext.add(new Position(row + s[0], col + s[1]));
					}
				}
				curr.nextPos = allNext.toArray(new Position[allNext.size()]);
				curr.value = standValue[(col + (row - start.row) + width) % width];
				entityArray[row][col] = curr;
			}
		}
		// 调整棋盘格子下一可能性的优先级顺序，下一可能性对应的再下一可能性小的排在最前面。有限搜索策略
		for (int row = 0; row < height; row++) {// 行号
			for (int col = 0; col < width; col++) {// 列号
				Position[] source = entityArray[row][col].nextPos;
				Arrays.sort(source, new Comparator<Position>() {
					@Override
					public int compare(Position arg0, Position arg1) {
						return entityArray[arg0.row][arg0.col].nextPos.length
								- entityArray[arg1.row][arg1.col].nextPos.length;
					}
				});
				entityArray[row][col].nextPos = source;
			}
		}
		// 初始化路径
		result.addFirst(start);
	}

	/**
	 * 求解
	 * 
	 * @return
	 * @author:luohj
	 */
	LinkedList<Position> getResult(LinkedList<Position> list) {
		while (list.size() != height * width) {
			time = time.add(BigInteger.ONE);
			if (list.size() > (height * width)) {
				throw new RuntimeException("运行时，链表溢出！");
			}
			if (list.size() == 0) {
				break;
			}
			Position last = list.getLast();
			Position next = entityArray[last.row][last.col].getNextPos();
			if (next == null) {
				entityArray[last.row][last.col].cnt = 0;
				list.removeLast();
			} else {
				// 判断当前获取的下一个在链表中是否已经存在，存在返回空。
				if (!list.contains(next)) {
					if (next.equals(end) && list.size() == (height * width - 1)) {
						list.addLast(next);
						break;
					} else if (next.equals(end) && list.size() != (height * width - 1)) {
						continue;
					} else {
						list.addLast(next);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 左闭右开
	 * @param start
	 * @param end
	 * @return
	 * @author:luohj
	 */
	boolean between(int nbr, int start, int end) {
		if (nbr >= 0 && nbr < end) {
			return true;
		}
		return false;
	}

	/**
	 * 棋盘格子实体<br>
	 * 包括<br>
	 * 位置信息、下一步位置、计数器
	 * 
	 * @author luohj
	 *
	 */
	public class Entity {
		/**
		 * 当前位置
		 */
		private Position pos;
		/**
		 * 下一可能位置
		 */
		private Position[] nextPos;
		/**
		 * 计数器
		 */
		private int cnt = 0;
		/**
		 * 当前位置的值
		 */
		private int value;
		/**
		 * 当前步数
		 */
		private int step;

		/**
		 * @param row
		 * @param col
		 */
		public Entity(int row, int col) {
			Position p = new Position(row, col);
			this.pos = p;
		}

		public Position getNextPos() {
			if (cnt < nextPos.length)
				return nextPos[cnt++];
			else
				return null;
		}

		public void print() {
			System.out.print(step + "(" + value + ")\t");
		}

		public void printBold() {
			System.out.print("[" + step + "(" + value + ")]\t");
		}

		/**
		 * @return the pos
		 */
		public Position getPos() {
			return pos;
		}

		/**
		 * @param pos
		 *            the pos to set
		 */
		public void setPos(Position pos) {
			this.pos = pos;
		}

		/**
		 * @return the value
		 */
		public int getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(int value) {
			this.value = value;
		}

		/**
		 * @return the step
		 */
		public int getStep() {
			return step;
		}

		/**
		 * @param step
		 *            the step to set
		 */
		public void setStep(int step) {
			this.step = step;
		}
	}

	/**
	 * 位置信息
	 * 
	 * @author luohj
	 *
	 */
	public static class Position {
		/**
		 * 列号，0开始
		 */
		private int col;
		/**
		 * 行号，0开始
		 */
		private int row;

		/**
		 * @param row
		 * @param col
		 */
		public Position(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public boolean equals(Object pos) {
			return this.row == ((Position) pos).row && this.col == ((Position) pos).col;
		}

		public void println() {
			System.out.println("行：" + this.row + "，列：" + this.col);
		}
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the startValue
	 */
	public int getStartValue() {
		return startValue;
	}

	/**
	 * @param startValue
	 *            the startValue to set
	 */
	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
