import "./NewArticle.css"
import React, {useState} from "react";
import MethodPreview from "../Method/MethodPreview/method-preview";
import {Dropdown, DropdownButton} from "react-bootstrap";
import {FaTimes} from "react-icons/all";
import {getSectionsForPreview, getSectionsForSubmit} from "../../utils/sections";
import {PROTOCOL} from "../../constants";
import Preloader from "../common/Preloader/preloader";


const NewArticleView = ({article, category, onSubmit, sectionTitles}) => {

    const getNewSection = (sectionName) => {
        return {
            name: sectionName ? sectionName : "",
            content: {
                text: "",
            }
        }
    }

    const [preview, setPreview] = useState(false);
    const [sections, setSections] = useState([getNewSection(sectionTitles[0])]);
    const [methodName, setMethodName] = useState("")

    const addNewSection = () => {
        const section = getNewSection()
        setSections(oldSections => [...oldSections, section]);
    }

    const addSectionEnabled = () => {
        const lastSection = sections[sections.length - 1];
        return lastSection.name.length > 0 && lastSection.content.text.length > 0;
    }

    const handleMethodNameChange = (e) => {
        setMethodName(e.target.value)
    }

    const handleSectionContentUpdate = (e, index) => {
        let newSections = [...sections]
        newSections[index].content.text = e.target.value;
        setSections(newSections);
    };

    const handleSectionTitleSelect = (title, index) => {
        let newSections = [...sections];
        newSections[index].name = title;
        setSections(newSections);
    }

    const handlePreview = () => {
        setPreview(!preview);
    }

    function sectionCanBeChosen(title) {
        if (title === PROTOCOL) return false
        for (const section of sections) {
            if (section.name === title) {
                return false
            }
        }
        return true
    }

    function submitDisabled() {
        return sections[0].content.text.length === 0 || methodName.length === 0;
    }

    function removeSection(index) {
        const newSections = [...sections];
        newSections.splice(index, 1);
        setSections(newSections);
    }

    function isNewProtocol() {
        return !!article
    }

    function handleSubmit(e) {
        e.preventDefault();
        onSubmit(isNewProtocol() ? sections : getSectionsForSubmit(sections), methodName)
    }

    function getHeaderBlock() {
        return (
            <h4>
                {isNewProtocol() ? ("Article: " + article.version?.name)
                    : ("Category: " + category?.name)}
            </h4>
        );
    }

    function getNameField() {
        return (
            <h4>
                {isNewProtocol() ? "Protocol name" : "Method name"}
            </h4>
        );
    }

    function getNamePlaceholder() {
        return isNewProtocol() ? "Input protocol name" : "Input method name"
    }

    if (!category && !article) return <Preloader/>

    if (preview) return <MethodPreview name={methodName}
                                       sections={isNewProtocol() ? sections : getSectionsForPreview(sections)}
                                       goBack={() => setPreview(false)}/>

    return (
        <form className="new-article-form">
            {getHeaderBlock()}
            <div>
                <div className="new-article-form__method-name">
                    <h2 className="col-form-label">
                        {getNameField()}
                    </h2>
                    <textarea className="form-control"
                              onChange={handleMethodNameChange}
                              value={methodName}
                              placeholder={getNamePlaceholder()}
                    />
                </div>
                <div className="form-group">
                    <h2 className="col-form-label">
                        {sectionTitles[0]}
                    </h2>
                    <textarea className="form-control new-article-form__section-content"
                              onChange={
                                  (e) => handleSectionContentUpdate(e, 0)
                              }
                              value={sections[0].content.text}
                              placeholder="Text"
                    />
                </div>
                {sections.map((section, index) => {
                    if (index === 0) return
                    return (
                        <div className="form-group">
                            <div className="new-article-form__section-title-row">
                                <DropdownButton variant="light" id={"choose-section-" + index}
                                                title={sections[index].name ? sections[index].name : "Choose section"}>
                                    {
                                        sectionTitles

                                            .filter(sectionCanBeChosen)
                                            .map(type => (
                                                <Dropdown.Item
                                                    eventKey={type}
                                                    onSelect={(title) => handleSectionTitleSelect(title, index)}>
                                                    {type}
                                                </Dropdown.Item>
                                            ))
                                    }
                                </DropdownButton>
                                <FaTimes onClick={() => removeSection(index)}
                                         className="new-article-form__remove-icon"/>
                            </div>
                            <textarea className="form-control new-article-form__section-content"
                                      onChange={
                                          (e) => handleSectionContentUpdate(e, index)
                                      }
                                      value={sections[index].content.text}
                                      placeholder="Text"
                            />
                        </div>
                    );
                })}
            </div>
            <div className="d-flex bd-highlight mb-3">
                {
                    !article &&
                    <button
                        className={"btn btn-large btn-primary new-article-form__button mr-auto p-2 bd-highlight"}
                        onClick={addNewSection} disabled={!addSectionEnabled()}>Add Section
                    </button>
                }
                <button type="submit"
                        className="btn btn-large btn-secondary new-article-form__button p-2 bd-highlight"
                        disabled={submitDisabled()}
                        onClick={handlePreview}>Preview
                </button>
                <button type="submit"
                        className="btn btn-large btn-success new-article-form__button p-2 bd-highlight"
                        disabled={submitDisabled()}
                        onClick={handleSubmit}>Submit
                </button>
            </div>
        </form>
    )


}

export default NewArticleView