package com.demo.carbooking.domain.concept;

import lombok.Getter;
import lombok.Setter;

public abstract class TrackDomainObject implements ValueObject {
    @Setter
    @Getter
    protected TrackState trackState;

    protected TrackDomainObject() {
        this.trackState = TrackState.NONE;
    }

    public boolean trackCreated() {
        return this.getTrackState() == TrackState.CREATED;
    }

    public boolean trackUpdated() {
        return this.getTrackState() == TrackState.UPDATED;
    }

    public boolean trackDeleted() {
        return this.trackState == TrackState.DELETED;
    }

    public boolean trackNone() {
        return this.trackState == TrackState.NONE;
    }

    public void markAsUpdated() {
        if (this.getTrackState() == TrackState.NONE) {
            this.trackState = TrackState.UPDATED;
        }
    }

    public void markAsCreated() {
        this.trackState = TrackState.CREATED;
    }

    public void markAsDeleted() {
        this.trackState = TrackState.DELETED;
    }
}
