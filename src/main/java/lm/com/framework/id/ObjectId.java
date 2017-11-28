/**
 *      Copyright (C) 2008 10gen Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package lm.com.framework.id;

import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ObjectId
 * <p>
 * 仿Mongodb中的ObjectId生成规则 <br />
 * 12个字节，24字符串长度, 分割规则如下: <blockquote>
 * 
 * <pre>
 * <table border="1">
 * <tr><td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td>
 *     <td>7</td><td>8</td><td>9</td><td>10</td><td>11</td></tr>
 * <tr><td colspan="4">time</td><td colspan="3">machine</td>
 *     <td colspan="2">pid</td><td colspan="3">inc</td></tr>
 * </table>
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @dochub objectid
 */
public class ObjectId implements Comparable<ObjectId>, java.io.Serializable {
	private static final long serialVersionUID = -4415279469780082174L;

	private static final Logger logger = LoggerFactory.getLogger(ObjectId.class);

	/**
	 * 线程安全的下一个随机数,每次生成自增+1
	 */
	// 随机
	private static AtomicInteger _nextInc = new AtomicInteger((new java.util.Random()).nextInt());

	private final int _time;
	private final int _machine;
	private final int _inc;
	private boolean _new;

	/**
	 * 机器信息
	 */
	private static final int _genmachine;
	/**
	 * 初始化机器信息 = 机器码 + 进程码
	 */
	static {
		try {
			// build a 2-byte machine piece based on NICs info
			final int machinePiece;
			{
				StringBuilder sb = new StringBuilder();
				// 返回机器所有的网络接口
				Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
				// 遍历网络接口
				while (e.hasMoreElements()) {
					NetworkInterface ni = e.nextElement();
					sb.append(ni.toString());
				}
				// 保留后两位
				machinePiece = sb.toString().hashCode() << 16;
				logger.info("machine piece post: " + Integer.toHexString(machinePiece));
			}

			// add a 2 byte process piece. It must represent not only the JVM
			// but the class loader.
			// Since static var belong to class loader there could be collisions
			// otherwise
			// 进程码
			// 因为静态变量类加载可能相同,所以要获取进程ID + 加载对象的ID值
			final int processPiece;
			{
				int processId = new java.util.Random().nextInt();
				try {
					// 获取进程ID
					processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
				} catch (Throwable t) {
				}

				ClassLoader loader = ObjectId.class.getClassLoader();
				// 返回对象哈希码,无论是否重写hashCode方法
				int loaderId = loader != null ? System.identityHashCode(loader) : 0;

				// 进程ID + 对象加载ID
				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toHexString(processId));
				sb.append(Integer.toHexString(loaderId));
				// 保留前2位
				processPiece = sb.toString().hashCode() & 0xFFFF;
				logger.info("process piece: " + Integer.toHexString(processPiece));
			}

			// 生成机器信息 = 取机器码的后2位和进程码的前2位
			_genmachine = machinePiece | processPiece;
			logger.info("machine : " + Integer.toHexString(_genmachine));
		} catch (java.io.IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * Create a new object id.
	 */
	public ObjectId() {
		_time = (int) (System.currentTimeMillis() / 1000);
		_machine = _genmachine;
		_inc = _nextInc.getAndIncrement();
		_new = true;
	}

	public ObjectId(Date time) {
		this(time, _genmachine, _nextInc.getAndIncrement());
	}

	public ObjectId(Date time, int inc) {
		this(time, _genmachine, inc);
	}

	public ObjectId(Date time, int machine, int inc) {
		_time = (int) time.getTime();
		_machine = machine;
		_inc = inc;
		_new = false;
	}

	/**
	 * Creates a new instance from a string.
	 * 
	 * @param s
	 *            the string to convert
	 * @throws IllegalArgumentException
	 *             if the string is not a valid id
	 */
	public ObjectId(String s) {
		this(s, false);
	}

	public ObjectId(String s, boolean babble) {
		if (!isValid(s))
			throw new IllegalArgumentException("invalid ObjectId [" + s + "]");

		if (babble)
			s = babbleToMongod(s);

		byte b[] = new byte[12];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
		}
		ByteBuffer bb = ByteBuffer.wrap(b);
		_time = bb.getInt();
		_machine = bb.getInt();
		_inc = bb.getInt();
		_new = false;
	}

	public ObjectId(byte[] b) {
		if (b.length != 12)
			throw new IllegalArgumentException("need 12 bytes");
		ByteBuffer bb = ByteBuffer.wrap(b);
		_time = bb.getInt();
		_machine = bb.getInt();
		_inc = bb.getInt();
		_new = false;
	}

	/**
	 * Creates an ObjectId
	 * 
	 * @param time
	 *            time in seconds
	 * @param machine
	 *            machine ID
	 * @param inc
	 *            incremental value
	 */
	public ObjectId(int time, int machine, int inc) {
		_time = time;
		_machine = machine;
		_inc = inc;
		_new = false;
	}

	// region 属性
	public int getMachine() {
		return _machine;
	}

	public int setMachine() {
		return _machine;
	}

	/**
	 * Gets the time of this ID, in milliseconds
	 * 
	 * @return
	 */
	public int getTime() {
		return _time;
	}

	public int setTime() {
		return _time;
	}

	public int getInc() {
		return _inc;
	}

	public int setInc() {
		return _inc;
	}

	public boolean getNew() {
		return _new;
	}

	public void notNew() {
		_new = false;
	}

	/**
	 * Gets the time of this ID, in milliseconds
	 * 
	 * @return
	 */
	public long getTimeMillisecond() {
		return _time * 1000L;
	}
	// endregion

	/**
	 * 生成ObjectId
	 * 
	 * @return 新ObjectId
	 */
	public static ObjectId newObjectId() {
		return new ObjectId();
	}

	/**
	 * 检查<code>ObjectId</code>是否有效
	 * 
	 * @return boolean
	 */
	public static boolean isValid(String s) {
		if (s == null)
			return false;

		final int len = s.length();
		if (len != 24)
			return false;

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c >= '0' && c <= '9')
				continue;
			if (c >= 'a' && c <= 'f')
				continue;
			if (c >= 'A' && c <= 'F')
				continue;

			return false;
		}

		return true;
	}

	/**
	 * 把Object转换成ObjectId
	 * 
	 * @param o
	 * @return <code>ObjectId</code>
	 */
	public static ObjectId toObjectId(Object o) {
		if (o == null)
			return null;

		if (o instanceof ObjectId)
			return (ObjectId) o;

		if (o instanceof String) {
			String s = o.toString();
			if (isValid(s))
				return new ObjectId(s);
		}

		return null;
	}

	/**
	 * 重写toString() 把ObjectId转换为字符串
	 */
	@Override
	public String toString() {
		return toStringMongoId();
	}

	/**
	 * 转换成字节码
	 * 
	 * @return
	 */
	public byte[] toByteArray() {
		byte b[] = new byte[12];
		ByteBuffer bb = ByteBuffer.wrap(b);
		// by default BB is big endian like we need
		bb.putInt(_time);
		bb.putInt(_machine);
		bb.putInt(_inc);
		return b;
	}

	/**
	 * 转成long
	 * 
	 * @return
	 */
	public long toLong() {
		byte[] b = toByteArray();
		ByteBuffer bb = ByteBuffer.wrap(b);
		return bb.getLong();
	}

	/**
	 * long 转 ObjectId
	 * 
	 * @param l
	 * @param inc
	 * @return
	 */
	public ObjectId longToObjectId(long l, int inc) {
		byte[] b = new byte[12];
		ByteBuffer bb2 = ByteBuffer.wrap(b);
		bb2.putLong(l);
		bb2.putInt(inc);
		return new ObjectId(b);
	}

	/**
	 * 转换成MongoId字符串
	 * 
	 * @return
	 */
	private String toStringMongoId() {
		byte b[] = toByteArray();

		StringBuilder buf = new StringBuilder(24);

		for (int i = 0; i < b.length; i++) {
			int x = b[i] & 0xFF;
			String s = Integer.toHexString(x);
			if (s.length() == 1)
				buf.append("0");
			buf.append(s);
		}

		return buf.toString();
	}

	public String toStringBabble() {
		return babbleToMongod(toStringMongoId());
	}

	public static String babbleToMongod(String b) {
		if (!isValid(b))
			throw new IllegalArgumentException("invalid object id: " + b);

		StringBuilder buf = new StringBuilder(24);
		for (int i = 7; i >= 0; i--)
			buf.append(_pos(b, i));
		for (int i = 11; i >= 8; i--)
			buf.append(_pos(b, i));

		return buf.toString();
	}

	public int hashCode() {
		int x = _time;
		x += (_machine * 111);
		x += (_inc * 17);
		return x;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;

		ObjectId other = toObjectId(o);
		if (other == null)
			return false;

		return _time == other._time && _machine == other._machine && _inc == other._inc;
	}

	private static String _pos(String s, int p) {
		return s.substring(p * 2, (p * 2) + 2);
	}

	private int _compareUnsigned(int i, int j) {
		long li = 0xFFFFFFFFL;
		li = i & li;
		long lj = 0xFFFFFFFFL;
		lj = j & lj;
		long diff = li - lj;
		if (diff < Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		if (diff > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int) diff;
	}

	public int compareTo(ObjectId id) {
		if (id == null)
			return -1;

		int x = _compareUnsigned(_time, id._time);
		if (x != 0)
			return x;

		x = _compareUnsigned(_machine, id._machine);
		if (x != 0)
			return x;

		return _compareUnsigned(_inc, id._inc);
	}

	/**
	 * Gets the generated machine ID, identifying the machine / process / class
	 * loader
	 * 
	 * @return
	 */
	public static int getGenMachineId() {
		return _genmachine;
	}

	/**
	 * Gets the current value of the auto increment
	 * 
	 * @return
	 */
	public static int getCurrentInc() {
		return _nextInc.get();
	}

	public static int _flip(int x) {
		int z = 0;
		z |= ((x << 24) & 0xFF000000);
		z |= ((x << 8) & 0x00FF0000);
		z |= ((x >> 8) & 0x0000FF00);
		z |= ((x >> 24) & 0x000000FF);
		return z;
	}
}
