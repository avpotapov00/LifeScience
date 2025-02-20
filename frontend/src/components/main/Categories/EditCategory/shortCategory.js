import React from "react";
import {ROLES} from "../../../../constants";
import {withRouter} from "react-router";
import Preloader from "../../../common/Preloader/preloader";
import {connect} from "react-redux";
import {
    clearCategory,
    deleteCategoryThunk,
    getCategoryThunk
} from "../../../../redux/actions/category-actions";
import {
    Button,
    Card
} from "react-bootstrap";
import "./editCategory.css"
import SendByUrlButton from "../../../common/Button/sendByUrlButton";

class ShortCategory extends React.Component {
    constructor(props) {
        super(props);
        this.onDelete = this.onDelete.bind(this);
        this.onEdit = this.onEdit.bind(this);
        this.onAdd = this.onAdd.bind(this);
    }

    componentDidMount() {
        this.refreshCategories();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.match.params.id !== prevProps.match.params.id ) {
            this.refreshCategories();
        }
    }

    componentWillUnmount() {
        this.props.clearCategory()
    }

    refreshCategories() {
        const id = this.props.match.params.id;
        this.props.getCategoryThunk(id);
    }


    onDelete(event) {
        event.preventDefault();
        this.props.deleteCategoryThunk(this.props.match.params.id);
        this.props.history.push(`/categories/${this.props.category.parentId}`)
    }

    onEdit(event) {
        event.preventDefault();
        this.props.history.push(`/category/edit/${this.props.category.id}`)
    }

    onAdd(event) {
        event.preventDefault();
        this.props.history.push(`/category/add/${this.props.category.id}`)
    }

    render() {
        if (!this.props.isReceived) {
            return <Preloader/>
        }

        const addButton = () => {
            if (this.props.category.articles.length === 0) {
                return(
                    <Button variant="success" size="lg" block onClick={this.onAdd}>
                        Add new category to this
                    </Button>
                );
            }
        }

        const deleteButton = () => {
            if (this.props.category.articles.length === 0
                && this.props.category.subcategories.length === 0
                && this.props.category.id !== 1) {
                return(
                    <Button variant="danger" size="lg" block onClick={this.onDelete}>
                        Delete Category
                    </Button>
                );
            }
        }

        const editButton = () => {
            if (this.props.category.id !== 1) {
                return(
                    <Button variant="secondary" size="lg" block onClick={this.onEdit}>
                        Edit Category
                    </Button>
                );
            }
        }

        const showingButtons = () => {
            if (!this.props.user || !(this.props.user.roles.includes(ROLES.admin) || this.props.user.roles.includes(ROLES.moderator)) || this.props.match.params.id === 1) {
                return;
            }

            return(
                <div className="buttons">
                    {editButton()}
                    {addButton()}
                    {deleteButton()}
                </div>
            );
        }

        const data = {
            name: this.props.category.name,
            parentId: this.props.category.parentId,
            order: this.props.category.order
        }

        return (
            <div>
                <div className="buttons_container">
                    <SendByUrlButton message="Previous" url={"/categories/" + this.props.match.params.id} {...this.props} />
                </div>
                <div className="short_category_container">
                    <Card>
                        <Card.Body>
                            <Card.Title>{data.name}</Card.Title>
                            <Card.Text>
                                Category has showing priority: {data.order}
                            </Card.Text>
                            {showingButtons()}
                        </Card.Body>
                    </Card>
                </div>
            </div>
        )
    }
}

let mapStateToProps = (state) => {
    return({
        isReceived: state.categoryPage.isReceived,
        category: state.categoryPage.category
    });
}

export default connect(mapStateToProps, {getCategoryThunk, clearCategory, deleteCategoryThunk})(withRouter(ShortCategory));
