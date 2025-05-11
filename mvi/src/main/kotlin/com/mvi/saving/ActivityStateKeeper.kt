package com.mvi.saving

class ActivityStateKeeper(
    private val stateKeeper: SavedStateKeeper
) : SavedStateKeeper by stateKeeper
