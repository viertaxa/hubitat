/*
* 
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
* 
*/


/*
*
* RELEASE HISTORY:
*     - 0.1.0: Initial Release
*
*/

definition(
    name: "Minimum Sonos Volume Tracker",
    namespace: "com.viertaxa.hubitat",
    author: "Taylor Vierrether",
    description: "Get the minimum Sonos volume",
    category: "Convenience",
    iconUrl: "",
    iconX2Url: "")

preferences {
	page(name: "mainPage")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "Sonos Minimum Volume", install: true, uninstall: true) {
		section {
			input "thisName", "text", title: "Name this Sonos minimum finder", submitOnChange: true
			if(thisName) app.updateLabel("$thisName")
			input "sonoses", "capability.musicPlayer", title: "Select Sonos Devices", submitOnChange: true, required: true, multiple: true
            input "enableLogDebug", "bool", title: "Enable Debug Logging", submitOnChange: true, required: true, defaultValue: false
			if(sonoses) paragraph "Current Sonos minimum is ${minSonosVolume()}"
		}
	}
}

def installed() {
    logTrace "Entering: installed()"
    logTrace "Calling initialize()"
	initialize()
    logTrace "Exiting: installed()"
}

def updated() {
    logTrace "Entering: updated()"
    logTrace "calling unsubscribe()"
	unsubscribe()
    logTrace "calling initialize()"
	initialize()
    logTrace "Exiting: updated()"
}

def initialize() {
    logTrace "Entering: initialize()"
    logTrace "getting minDev object"
	def minDev = getChildDevice("MinimumSonosVolume${app.id}")
	if(!minDev) {
        logDebug "minDev not found. Creating it."
        minDev = addChildDevice("hubitat", "Virtual audioVolume", "MinimumSonosVolume${app.id}", null, [label: thisName, name: thisName])
        logDebug "Created minDev ${minDev}"
    }
    logTrace "calling setvolume(minSonosVolume()) on minDev"
	minDev.setVolume(minSonosVolume())
    logTrace "Subscribing to sonos update events"
	subscribe(sonoses, "handler", [filterEvents: false])
    logTrace "Exiting: initialize"
}

def Integer minSonosVolume() {
    logTrace "Entering minSonosVolume()"
    logTrace "Getting minimum sonos volume"
	def minVolume = sonoses.collect{
        it.currentStates.findAll{ it.name == "volume"}.collect{ a -> a.value}.min()
    }.min()
    logDebug "Found minimum volume $minVolume"
    logTrace "Exiting: minSonosVolume"
    minVolume.toInteger()
}

def handler(evt) {
    logTrace "Entering: handler(evt)"
    logTrace "Getting minDev"
	def minDev = getChildDevice("MinimumSonosVolume${app.id}")

    logTrace "Calling setVolume(minSonosVolume()) on minDev"
	minDev.setVolume(minSonosVolume())
	logDebug "Minimum Sonos Volume = ${minSonosVolume()}"
    logTrace "Exiting: handler(evt)"
}

def logInfo(thing) {
    log.info thing
}

def logDebug(thing) {
    if (enableLogDebug) log.debug thing
}

def logTrace(thing) {
    //enable during development only!
    if (false) log.trace thing
}