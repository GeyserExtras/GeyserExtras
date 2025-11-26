package dev.letsgoaway.geyserextras.core.cache;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheDates {
    @Setter
    @Getter
    public long dataUpdateTime = -1;

    @Setter
    @Getter
    public int[] lastExtrasPackVersion = {0, 0, 0};

    public CacheDates() {
    }
}
