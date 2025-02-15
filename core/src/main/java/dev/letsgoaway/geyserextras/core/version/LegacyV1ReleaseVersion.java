package dev.letsgoaway.geyserextras.core.version;

public class LegacyV1ReleaseVersion {
    public Version3 minecraftVersion;
    public Version3 resourceVersion;

    public boolean prerelease = false;

    LegacyV1ReleaseVersion(Version3 minecraftVersion, Version3 resourceVersion) {
        this.minecraftVersion = minecraftVersion;
        this.resourceVersion = resourceVersion;
    }

    LegacyV1ReleaseVersion(Version3 minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
        this.resourceVersion = resourceVersion;
    }

    LegacyV1ReleaseVersion(Version3 minecraftVersion, boolean prerelease) {
        this.minecraftVersion = minecraftVersion;
        this.resourceVersion = resourceVersion;
        this.prerelease = prerelease;
    }

    LegacyV1ReleaseVersion(String string) {
        String[] versions = string.split("-v");
        if (string.contains("+v")) {
            versions = string.split("\\+v");
            this.prerelease = true;
        }
        this.minecraftVersion = Version3.fromString(versions[0]);
        this.resourceVersion = Version3.fromString(versions[1]);
    }

    public boolean isNewer(LegacyV1ReleaseVersion v) {
        return this.resourceVersion.isNewer(v.resourceVersion);
    }

    public String toString() {
        String middle = "-v";
        if (this.prerelease) {
            middle = "+v";
        }
        return this.minecraftVersion.toString() + middle + this.resourceVersion.toString();
    }

}
