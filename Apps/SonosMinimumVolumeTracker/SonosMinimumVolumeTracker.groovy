/*
* Copyright 2020 Taylor Vierrether
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

/*
*
* RELEASE HISTORY:
*     - 0.1.0: Initial Release
*     - 0.1.1: Add support for uninstalled() callback
*     - 0.1.2: Change input selection to show only Sonos devices
*     - 0.1.3: Comply with Linter suggestions where appropriate
*
*/

import groovy.transform.Field

definition(
    name: 'Minimum Sonos Volume Tracker',
    namespace: 'com.viertaxa.hubitat',
    author: 'Taylor Vierrether',
    description: 'Get the minimum Sonos volume',
    category: 'Convenience',
    iconUrl: '',
    iconX2Url: ''
)

@Field
String mainPageID = 'mainPage'

preferences {
    logTrace 'Entering preferences closure'
    page(name: mainPageID)
    logTrace 'Exiting preferences closure'
}

def mainPage() {
    logTrace 'Entering mainPage()'
    logTrace 'Calling dynamicPage()'
    return dynamicPage(name: mainPageID, title: 'Sonos Minimum Volume', install: true, uninstall: true) {
        logTrace 'Calling Section'
        section {
            logTrace 'Calling input for thisName'
            input 'thisName', 'text', title: 'Name this Sonos minimum finder', submitOnChange: true
            if (thisName) { app.updateLabel("$thisName") }
            logTrace 'Calling input for sonoses'
            input 'sonoses', 'device.SonosPlayer', title: 'Select Sonos Devices',
                submitOnChange: true, required: true, multiple: true
            logTrace 'calling input for enableLogDebug'
            input 'enableLogDebug', 'bool', title: 'Enable Debug Logging',
                submitOnChange: true, required: true, defaultValue: false
            if (sonoses) { paragraph "Current Sonos minimum is ${minSonosVolume()}" }
        }
    }
}

void installed() {
    logTrace 'Entering: installed()'
    logTrace 'Calling initialize()'
    initialize()
    logTrace 'Exiting: installed()'
}

void uninstalled() {
    logTrace 'Entering: uninstalled()'
    logTrace 'Calling unsubsscribe'
    unsubscribe()
    logTrace 'calling deleteChildDevice()'
    deleteChildDevice("MinimumSonosVolume${app.id}")
    logTrace 'Exiting: uninstalled()'
}

void updated() {
    logTrace 'Entering: updated()'
    logTrace 'calling unsubscribe()'
    unsubscribe()
    logTrace 'calling initialize()'
    initialize()
    logTrace 'Exiting: updated()'
}

void initialize() {
    logTrace 'Entering: initialize()'
    logTrace 'getting minDev object'
    def minDev = getChildDevice("MinimumSonosVolume${app.id}")
    if (!minDev) {
        logDebug 'minDev not found. Creating it.'
        minDev = addChildDevice('hubitat', 'Virtual audioVolume',
            "MinimumSonosVolume${app.id}", null, [label: thisName, name: thisName])
        logDebug "Created minDev ${minDev}"
    }
    logTrace 'calling setvolume(minSonosVolume()) on minDev'
    minDev.volume = minSonosVolume()
    logTrace 'Subscribing to sonos update events'
    subscribe(sonoses, 'handler', [filterEvents: false])
    logTrace 'Exiting: initialize'
}

Integer minSonosVolume() {
    logTrace 'Entering minSonosVolume()'
    logTrace 'Getting minimum sonos volume'
    String minVolume = sonoses.collect { sonos ->
        sonos.currentStates.findAll { state ->
            state.name == 'volume' } *.value.min()
    }.min()
    logDebug "Found minimum volume $minVolume"
    logTrace 'Exiting: minSonosVolume'
    return minVolume.toInteger()
}

void handler(evt) {
    logTrace 'Entering: handler(evt)'
    logTrace "Triggered by Event: ${evt}"
    evt.properties.each { property -> logTrace "${property.key} => ${property.value}" }
    logTrace 'Getting minDev'
    def minDev = getChildDevice("MinimumSonosVolume${app.id}")

    logTrace 'Calling setVolume(minSonosVolume()) on minDev'
    minDev.setVolume minSonosVolume()
    logDebug "Minimum Sonos Volume = ${minSonosVolume()}"
    logTrace 'Exiting: handler(evt)'
}

void logInfo(message) {
    log.info message
}

void logDebug(message) {
    if (enableLogDebug) { log.debug message }
}

void logTrace(message) {
    //enable during development only!
    traceEnable = false
    if (traceEnable) { log.trace message }
}
