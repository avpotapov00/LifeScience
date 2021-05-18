import {AUTO_SECTION_TITLES, INFO_SECTION_TITLES, PROTOCOLS, SECTION_TITLES} from "../constants";

//todo rename
export const getSectionsForArticlePreview = (sections) => {
    const sortedSections = sortAndAddEmpty(sections, SECTION_TITLES)
    sortedSections.splice(1, 1, {name: PROTOCOLS})
    return sortedSections
}

export const getSectionsForSubmit = (sections) => {
    return sortAndAddEmpty(sections, INFO_SECTION_TITLES)
}

const pushAutoSections = (sections) => {
    for (const title of AUTO_SECTION_TITLES) {
        sections.push({
            name: title
        })
    }
}

export const getSectionsForProtocol = (sections) => {
    const sortedSections = sortSectionsBy(sections, INFO_SECTION_TITLES);
    pushAutoSections(sortedSections)
    return sortedSections
}

export const getSectionsForMain = (sections) => {
    const sortedSections = sortAndAddEmpty(sections, INFO_SECTION_TITLES);
    sortedSections.splice(1, 1, {name: PROTOCOLS})
    pushAutoSections(sortedSections)
    return sortedSections
}

const sortAndAddEmpty = (sections, example) => {
    const resultSections = []
    outer:
        for (const title of example) {
            for (const section of sections) {
                if (section.name === title) {
                    resultSections.push(section)
                    continue outer
                }
            }
            resultSections.push({
                name: title
            })
        }
    return resultSections
}

const sortSectionsBy = (sections, example) => {
    const sortedSections = [];
    for (const title of example) {
        for (const section of sections) {
            if (section.name === title) {
                sortedSections.push(section)
            }
        }
    }
    return sortedSections;
}