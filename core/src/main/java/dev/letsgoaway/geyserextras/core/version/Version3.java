package dev.letsgoaway.geyserextras.core.version;

import java.util.List;

public class Version3 {
    public int major;
    public int minor;
    public int patch;

    public Version3(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }

    public boolean isNewer(Version3 v) {
        return this.major > v.major || this.minor > v.minor || this.patch > v.patch;
    }

    public static Version3 fromString(String string) {
        String[] nums = string.split("\\.");
        return new Version3(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]), Integer.parseInt(nums[2]));
    }

    public static Version3 fromArray(int[] nums) {
        return new Version3(nums[0], nums[1], nums[2]);
    }

    public static Version3 fromList(List<Integer> nums) {
        return new Version3(nums.get(0), nums.get(1), nums.get(2));
    }

    public int[] asArray() {
        return new int[]{major, minor, patch};
    }
}
