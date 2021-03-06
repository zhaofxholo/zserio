package without_sources_amalgamation;

struct Tile
{
    uint8   version;
    uint32  numElements;
    uint8   data[numElements];
};

sql_table GeoMapTable
{
    int32   tileId sql "PRIMARY KEY";
    Tile    tile;
};

sql_database WorldDb
{
    GeoMapTable europe;
    GeoMapTable america;
};
