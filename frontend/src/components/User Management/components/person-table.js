import React from "react";
import Table from "../../../commons/tables/table";

// Function to handle delete action


const filters = [
    {
        accessor: 'name',
    }
];

class PersonTable extends React.Component {
    columns = [
        {
            Header: 'Name',
            accessor: 'name',
        },
        {
            Header: 'Email',
            accessor: 'email',
        },
        {
            Header: 'Role',
            accessor: 'role',
        },
        {
            Header: 'ID',
            accessor: 'id',
        },
        {
            Header: 'Actions',
            accessor: 'actions',
            Cell: (row) => (
                <div>
                    <button onClick={() => this.callbacks.update(row.original)}>Update</button>
                    <button onClick={() => this.callbacks.delete(row.original.id)}>Delete</button>
                </div>
            )
        }
    ];

    constructor(props) {
        super(props);
        this.state = {
            tableData: this.props.tableData
        };
        this.callbacks = {
            update: this.props.update,
            delete: this.props.delete
        }
    }

    render() {
        return (
            <Table
                data={this.state.tableData}
                columns={this.columns}
                search={filters}
                pageSize={5}
            />
        );
    }
}

export default PersonTable;
